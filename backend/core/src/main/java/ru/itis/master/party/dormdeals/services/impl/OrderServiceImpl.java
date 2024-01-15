package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.converters.OrderConverter;
import ru.itis.master.party.dormdeals.dto.order.NewOrderDto;
import ru.itis.master.party.dormdeals.dto.order.NewOrderMessageDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.models.order.OrderMessage;
import ru.itis.master.party.dormdeals.models.order.OrderProduct;
import ru.itis.master.party.dormdeals.repositories.*;
import ru.itis.master.party.dormdeals.services.NotificationService;
import ru.itis.master.party.dormdeals.services.OrderService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.itis.master.party.dormdeals.models.Product.State.ACTIVE;
import static ru.itis.master.party.dormdeals.models.order.Order.State.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMessageRepository orderMessageRepository;
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;

    private static void reserveProductAmounts(List<OrderProduct> orderProducts) {
        orderProducts.forEach(orderProduct -> {
            Product product = orderProduct.getProduct();
            int required = orderProduct.getCount();
            int available = product.getCountInStorage();

            //TODO: мейби выкидывать другое исключение если стейт != ACTIVE
            if (product.getState() == ACTIVE && required <= available) {
                product.setCountInStorage((short) (available - required));
            } else
                throw new NotEnoughException(Product.class, product.getId(), required, available);
        });
    }

    private static void returnReservedAmounts(List<OrderProduct> orderProducts) {
        orderProducts.forEach(orderProduct -> {
            Product product = orderProduct.getProduct();
            product.setCountInStorage((short) (product.getCountInStorage() + orderProduct.getCount()));
        });
    }

    @Override
    @Transactional
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findWithCustomerAndShopById(id)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", id));

        Pageable pageable = PageRequest.of(0, 20, Sort.by("addedDate"));
        Page<OrderMessage> orderMessages = orderMessageRepository.findAllByOrderId(id, pageable);
        return orderConverter.from(order, orderMessages);
    }

    @Override
    @Transactional
    public void createOrder(long userId, NewOrderDto newOrderDto) {
        Map<Long, Integer> productIdAndCount = newOrderDto.getProducts().stream().collect(Collectors.toMap(
                NewOrderDto.OrderProduct::getProductId,
                NewOrderDto.OrderProduct::getCount));

        List<Product> products = productRepository.findAllById(productIdAndCount.keySet());

        // group products by shopId
        Map<Long, List<OrderProduct>> shopIdAndOrderProducts = products.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getShop().getId(),
                        Collectors.mapping(
                                product -> OrderProduct.builder()
                                        .product(product)
                                        .count(productIdAndCount.get(product.getId()))
                                        .price(product.getPrice())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        shopIdAndOrderProducts.values().forEach(OrderServiceImpl::reserveProductAmounts);

        // create orders for each shop
        ZonedDateTime time = ZonedDateTime.now();
        List<Order> orders = shopIdAndOrderProducts.entrySet().stream()
                .map(entry -> {
                    float priceSum = (float) entry.getValue().stream()
                            .mapToDouble(product -> product.getPrice() * product.getCount())
                            .sum();
                    priceSum = (float) ((int) (priceSum * 100)) / 100;
                    return Order.builder()
                            .customer(userRepository.getReferenceById(userId))
                            .addedDate(time)
                            .shop(shopRepository.getReferenceById(entry.getKey()))
                            .price(priceSum)
                            .state(Order.State.IN_PROCESSING)
                            .products(new ArrayList<>())
                            .build();
                }).toList();


        // Save result
        orders = orderRepository.saveAll(orders);

        orders.forEach(order -> shopIdAndOrderProducts.get(order.getShop().getId())
                .forEach(orderProduct -> {
                    orderProduct.setOrder(order);
                    order.getProducts().add(orderProduct);
                }));
    }

    @Override
    @Transactional
    public void updateOrderState(long userId, Long orderId, Order.State state) {
        Order order = orderRepository.findWithShopById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

        // Is costumer wants to update status?
        if (Objects.equals(userId, order.getCustomer().getId())) {

            // cancel order if possible
            if (state == CANCELLED && order.getState().index() < CONFIRMED.index()) {
                order.setState(CANCELLED);
                returnReservedAmounts(order.getProducts());
                sendNotificationAfterUpdate(orderId);
                return;
            }

            // Is shop owner wants to update status?
        } else if (Objects.equals(userId, order.getShop().getOwner().getId())) {

            // normal changing of status or recovery from CANCELLED
            if (state.index() > order.getState().index()) {
                if (order.getState() == CANCELLED) reserveProductAmounts(order.getProducts());
                order.setState(state);
                sendNotificationAfterUpdate(orderId);
                return;

                // cancel order if possible
            } else if (state == CANCELLED && order.getState() != DELIVERED && order.getState() != CANCELLED) {
                order.setState(CANCELLED);
                returnReservedAmounts(order.getProducts());
                sendNotificationAfterUpdate(orderId);
                return;
            }
        }
        throw new NotAcceptableException("Have not permission");
    }

    @Override
    @Transactional
    public void addOrderMessage(long userId, Long orderId, NewOrderMessageDto orderMessage) {
        Order order = orderRepository.findWithShopById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

        // throw if message adder not consumer and not seller
        if (!Objects.equals(userId, order.getCustomer().getId()) &&
                !Objects.equals(userId, order.getShop().getOwner().getId())) {
            throw new NotAcceptableException("have not permission");
        }

        OrderMessage message = OrderMessage.builder()
                .order(order)
                .user(userRepository.getReferenceById(userId))
                .message(orderMessage.getMessage())
                .addedDate(ZonedDateTime.now())
                .build();

        order.getMessages().add(message);
    }

    @Override
    public Page<OrderDto> getShopOrders(long shopOwnerId, long shopId, Pageable pageable) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(Shop.class, "id", shopId));
        if (shop.getOwner().getId() != shopOwnerId)
            throw new NotAcceptableException("have not permission");

        Page<Order> orders = orderRepository.findAllWithCustomerByShopId(shopId, pageable);
        return orderConverter.fromForSeller(orders);
    }

    @Override
    public Page<OrderDto> getUserOrders(long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithShopByCustomerId(userId, pageable);
        return orderConverter.fromForCustomer(orders);
    }

    public void sendNotificationAfterUpdate(Long orderId) {
        Thread.ofVirtual().start(() -> {
            Order order = orderRepository.findWithShopById(orderId)
                    .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

            notificationService.sendNotificationOrder(order,
                    "Order " + orderId + " updated. Status: " + order.getState());
            notificationService.updateUnreadNotificationCountForUser(order.getCustomer().getId());
        });
    }
}

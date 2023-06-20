package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.converters.OrderConverter;
import ru.itis.master.party.dormdeals.dto.orders.NewOrderDto;
import ru.itis.master.party.dormdeals.dto.orders.NewOrderMessageDto;
import ru.itis.master.party.dormdeals.dto.orders.OrderDto;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.models.order.OrderMessage;
import ru.itis.master.party.dormdeals.models.order.OrderProduct;
import ru.itis.master.party.dormdeals.repositories.OrderRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.OrderService;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.itis.master.party.dormdeals.models.Product.State.ACTIVE;
import static ru.itis.master.party.dormdeals.models.Product.State.NOT_AVAILABLE;
import static ru.itis.master.party.dormdeals.models.order.Order.State.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductsRepository productsRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;

    private static void reserveProductAmounts(List<OrderProduct> orderProducts) {
        orderProducts.forEach(orderProduct -> {
            Product product = orderProduct.getProduct();
            int required = orderProduct.getCount();
            int available = product.getCountInStorage();

            if (product.getState() == ACTIVE && required <= available) {
                product.setCountInStorage((short) (available - required));
                if (required == available) product.setState(NOT_AVAILABLE);
            } else
                throw new NotEnoughException(Product.class, product.getId(), required, available);
        });
    }

    private static void returnReservedAmounts(Order order) {
        order.getProducts().forEach(orderProduct -> {
            Product product = orderProduct.getProduct();
            product.setCountInStorage((short) (product.getCountInStorage() + orderProduct.getCount()));
        });
    }

    @Override
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", id));
        return orderConverter.from(order);
    }

    @Override
    @Transactional
    public void createOrder(long userId, NewOrderDto newOrderDto) {
        Map<Long, Integer> productIdAndCount = newOrderDto.getProducts().stream().collect(Collectors.toMap(
                NewOrderDto.OrderProduct::getProductId,
                NewOrderDto.OrderProduct::getCount));

        List<Product> products = productsRepository.findAllProductWithShopByIdIn(productIdAndCount.keySet());

        // group products by shopId
        Map<Long, List<OrderProduct>> shopIdAndOrderProducts = new HashMap<>();
        for (Product product : products) {
            List<OrderProduct> list = shopIdAndOrderProducts.computeIfAbsent(product.getShop().getId(), k -> new ArrayList<>());
            list.add(OrderProduct.builder()
                    .product(product)
                    .count(productIdAndCount.get(product.getId()))
                    .price(product.getPrice())
                    .build());
        }

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
                            .user(userRepository.getReferenceById(userId))
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
        Order order = orderRepository.findOrderWithProductsById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

        // Is costumer wants to update status?
        if (Objects.equals(userId, order.getUser().getId())) {

            // cancel order if possible
            if (state == CANCELLED && order.getState().index() < CONFIRMED.index()) {
                order.setState(CANCELLED);
                returnReservedAmounts(order);
                return;
            }

            // Is shop owner wants to update status?
        } else if (Objects.equals(userId, order.getShop().getOwner().getId())) {

            // normal changing of status or recovery from CANCELLED
            if (state.index() > order.getState().index()) {
                if (order.getState() == CANCELLED) reserveProductAmounts(order.getProducts());
                order.setState(state);
                return;

                // cancel order if possible
            } else if (state == CANCELLED && order.getState() != DELIVERED) {
                order.setState(CANCELLED);
                returnReservedAmounts(order);
                return;
            }
        }
        throw new NotAcceptableException("Have not permission");
    }

    @Override
    @Transactional
    public void addOrderMessage(long userId, Long orderId, NewOrderMessageDto orderMessage) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

        // throw if message adder not consumer and not seller
        if (!Objects.equals(userId, order.getUser().getId()) &&
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

        Page<Order> orders = orderRepository.findAllWithUserAndShopsByShopId(shopId, pageable);
        return orderConverter.from(orders);
    }

    @Override
    public Page<OrderDto> getUserOrders(long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithUserAndShopsByUserId(userId, pageable);
        return orderConverter.from(orders);
    }
}

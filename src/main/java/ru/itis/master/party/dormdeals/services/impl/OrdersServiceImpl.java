package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.OrderDto.NewOrder;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderWithProducts;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.OrderConverter;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.models.OrderProduct;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.OrderProductsRepository;
import ru.itis.master.party.dormdeals.repositories.OrdersRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.OrdersService;
import ru.itis.master.party.dormdeals.utils.GetOrThrow;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrderConverter orderConverter;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final ShopsRepository shopsRepository;
    private final ProductsRepository productsRepository;
    private final OwnerChecker ownerChecker;
    private final UserRepository userRepository;
    private final GetOrThrow getOrThrow;

    @Override
    public OrderDto getOrder(Long id) {
        return orderConverter.from(getOrThrow.getOrderOrThrow(id, ordersRepository));
    }

    @Override
    public OrderDto createOrder(String userEmail, NewOrder newOrder) {
        User user = userRepository.getByEmail(userEmail).orElseThrow(() ->
                new NotFoundException("user with email <" + userEmail + "> not found"));

        Shop shop = shopsRepository.findById(newOrder.getShopId()).orElseThrow(() ->
                new NotFoundException("shop with id <" + newOrder.getShopId() + "> not found"));

        ZonedDateTime orderTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        Order order = Order.builder()
                .user(user)
                .shop(shop)
                .orderTime(orderTime)
                .userComment(newOrder.getUserComment())
                .price(0)
                .state(Order.State.IN_PROCESSING)
                .build();

        ordersRepository.save(order);

        return orderConverter.from(order);
    }

    @Override
    public OrderDto updateOrderState(Long id, Order.State state) {
        ownerChecker.checkOwnerShop(
                getOrder(id)
                        .getShop()
                        .getOwner()
                        .getId(),
                ownerChecker.initThisUser(userRepository));

        Order order = getOrThrow.getOrderOrThrow(id, ordersRepository);
        order.setState(state);

        return orderConverter.from(ordersRepository.save(order));
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        ownerChecker.checkOwnerOrder(getOrThrow.getOrderOrThrow(id, ordersRepository)
                .getUser().getId(), ownerChecker.initThisUser(userRepository));
        orderProductsRepository.deleteAllByOrderId(id);
        ordersRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> createOrder(String userEmail, List<CartProductDto> cartProductDtoList) {
        Map<Long, OrderDto> orderMap = new HashMap<>(); // shopId -> orderDto
        Set<Long> shopsIdSet = new HashSet<>();

        for (CartProductDto cartProductDto : cartProductDtoList) {
            Long productId = cartProductDto.getId();
            Product product = getOrThrow
                    .getProductOrThrow(productId, productsRepository);
            Long shopId = product.getShop().getId();

            if (!shopsIdSet.contains(shopId)) {
                shopsIdSet.add(shopId);

                NewOrder newOrder = NewOrder.builder()
                        .shopId(product
                                .getShop().getId())
                        .build();

                OrderDto orderDto = createOrder(userEmail, newOrder);
                orderMap.put(shopId, orderDto);
            }

            OrderDto orderDto = orderMap.get(product.getShop().getId());

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(getOrThrow
                            .getOrderOrThrow(
                                    orderDto.getId(),
                                    ordersRepository))
                    .count(cartProductDto.getCount())
                    .build();

            orderDto.setPrice(orderDto.getPrice() + orderProduct.getProduct().getPrice() * orderProduct.getCount());
            orderProductsRepository.save(orderProduct);
        }

        for (OrderDto orderDto : orderMap.values()) {
            updateOrderPrice(orderDto.getId(), orderDto.getPrice());
        }

        return new ArrayList<>(orderMap.values());
    }

    @Override
    public OrderWithProducts getAllProductsThisOrder(Long orderId) {
        Order order = getOrThrow.getOrderOrThrow(orderId, ordersRepository);

        List<OrderProduct> orderProductList = orderProductsRepository.findAllByOrderId(orderId);

        return OrderWithProducts.builder()
                .order(order)
                .orderProducts(orderProductList)
                .build();
    }

    private void updateOrderPrice(Long id, float price) {
        Order order = getOrThrow.getOrderOrThrow(id, ordersRepository);
        order.setPrice(price);

        ordersRepository.save(order);
    }
}

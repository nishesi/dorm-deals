package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.OrderDto.NewOrder;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderWithProducts;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.models.OrderProduct;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.repositories.OrderProductsRepository;
import ru.itis.master.party.dormdeals.repositories.OrdersRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.OrdersService;
import ru.itis.master.party.dormdeals.utils.GetOrThrow;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;

import java.util.*;

import static ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto.from;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final ProductsRepository productsRepository;
    private final OwnerChecker ownerChecker;
    private final UserRepository userRepository;
    private final GetOrThrow getOrThrow;

    @Override
    public OrderDto getOrder(Long id) {
        return from(getOrThrow.getOrderOrThrow(id, ordersRepository));
    }

    @Override
    public OrderDto createOrder(NewOrder newOrder) {
        Order order = Order.builder()
                .user(newOrder.getUser())
                .shop(newOrder.getShop())
                .orderTime(newOrder.getOrderTime())
                .userComment(newOrder.getUserComment())
                .price(0)
                .state(Order.State.IN_PROCESSING)
                .build();

        ordersRepository.save(order);

        return from(order);
    }

    @Override
    public OrderDto updateOrderState(Long id, Order.State state) {
        Order order = getOrThrow.getOrderOrThrow(id, ordersRepository);
        order.setState(state);

        return from(ordersRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> createOrder(List<ProductDtoCart> productDtoCartList) {
        Map<Long, OrderDto> orderMap = new HashMap<>(); // shopId -> orderDto
        Set<Long> shopsIdSet = new HashSet<>();

        for (ProductDtoCart productDtoCart : productDtoCartList) {
            Long productId = productDtoCart.getId();
            Product product = getOrThrow
                    .getProductOrThrow(productId, productsRepository);
            Long shopId = product.getShop().getId();

            if (!shopsIdSet.contains(shopId)) {
                shopsIdSet.add(shopId);

                NewOrder newOrder = NewOrder.builder()
                        .orderTime(new Date())
                        .user(ownerChecker
                                .initThisUser(userRepository))
                        .shop(product
                                .getShop())
                        .build();

                OrderDto orderDto = createOrder(newOrder);
                orderMap.put(shopId, orderDto);
            }

            OrderDto orderDto = orderMap.get(product.getShop().getId());

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(getOrThrow
                            .getOrderOrThrow(
                                    orderDto.getId(),
                                    ordersRepository))
                    .count(productDtoCart.getCountInCart())
                    .build();

            orderDto.setPrice(orderDto.getPrice() + orderProduct.getProduct().getPrice() * orderProduct.getCount());
            orderProductsRepository.save(orderProduct);
        }

        for (OrderDto orderDto: orderMap.values()) {
            updateOrderPrice(orderDto.getId(), orderDto.getPrice());
        }

        return (List<OrderDto>) orderMap.values();
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

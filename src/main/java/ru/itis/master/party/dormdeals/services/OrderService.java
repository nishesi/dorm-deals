package ru.itis.master.party.dormdeals.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itis.master.party.dormdeals.dto.orders.NewOrderDto;
import ru.itis.master.party.dormdeals.dto.orders.NewOrderMessageDto;
import ru.itis.master.party.dormdeals.dto.orders.OrderDto;
import ru.itis.master.party.dormdeals.models.order.Order;

public interface OrderService {
    void createOrder(long userId, NewOrderDto newOrderDto);

    OrderDto getOrder(Long id);

    void updateOrderState(long userId, Long orderId, Order.State state);

    void addOrderMessage(long userId, Long orderId, NewOrderMessageDto orderMessage);

    Page<OrderDto> getUserOrders(long userId, Pageable pageable);

    Page<OrderDto> getShopOrders(long shopId, Pageable pageable);
}

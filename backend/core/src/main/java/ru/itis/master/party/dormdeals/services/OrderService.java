package ru.itis.master.party.dormdeals.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itis.master.party.dormdeals.dto.order.NewOrderDto;
import ru.itis.master.party.dormdeals.dto.order.NewOrderMessageDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;

public interface OrderService {
    void createOrder(long userId, NewOrderDto newOrderDto);

    OrderDto getOrder(Long id);

    void updateOrderState(long userId, Long orderId, Order.State state);

    void addOrderMessage(long userId, Long orderId, NewOrderMessageDto orderMessage);

    Page<OrderDto> getUserOrders(long userId, Pageable pageable);

    Page<OrderDto> getShopOrders(long shopOwnerId, long shopId, Pageable pageable);
}

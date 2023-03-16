package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.OrderDto.NewOrder;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.models.Order;

public interface OrdersService {
    OrderDto getOrder(long id);

    OrderDto createOrder(NewOrder newOrder);

    OrderDto updateOrderState(Long id, Order.State state);

    void deleteOrder(Long id);
}

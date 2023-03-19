package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.OrderDto.NewOrder;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderWithProducts;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.models.Order;

import java.util.List;

public interface OrdersService {
    OrderDto getOrder(Long id);

    OrderDto createOrder(String userEmail, NewOrder newOrder);

    OrderDto updateOrderState(Long id, Order.State state);

    void deleteOrder(Long id);

    List<OrderDto> createOrder(String userEmail, List<CartProductDto> cartProductDtoList);

    OrderWithProducts getAllProductsThisOrder(Long orderId);
}

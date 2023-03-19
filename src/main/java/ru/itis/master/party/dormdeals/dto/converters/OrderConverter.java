package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.models.Order;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final UserConverter userConverter;

    private final ShopConverter shopConverter;

    public OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .user(userConverter.from(order.getUser()))
                .orderTime(order.getOrderTime())
                .userComment(order.getUserComment())
                .shop(shopConverter.from(order.getShop()))
                .price(order.getPrice())
                .build();
    }

    public List<OrderDto> from(List<Order> orders) {
        return orders
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

}

package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.order.OrderMessageDto;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.models.order.OrderMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final UserConverter userConverter;

    public OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customer(userConverter.from(order.getCustomer()))
                .shop(from(order.getShop()))
                .orderMessages(from(order.getMessages()))
                .addedDate(order.getAddedDate())
                .price(order.getPrice())
                .build();
    }

    public Page<OrderDto> from(Page<Order> orders) {
        return orders.map(order -> OrderDto.builder()
                .id(order.getId())
                .customer(from(order.getCustomer()))
                .shop(from(order.getShop()))
                .addedDate(order.getAddedDate())
                .price(order.getPrice())
                .build());
    }

    private ShopDto from(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }

    private UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .build();
    }

    private List<OrderMessageDto> from(List<OrderMessage> orderMessages) {
        return orderMessages.stream()
                .map(orderMessage -> OrderMessageDto.builder()
                        .addedDate(orderMessage.getAddedDate())
                        .userId(orderMessage.getUser().getId())
                        .message(orderMessage.getMessage())
                        .build())
                .toList();
    }
}

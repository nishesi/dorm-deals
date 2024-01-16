package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.order.OrderProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.order.OrderMessageDto;
import ru.itis.master.party.dormdeals.models.jpa.Shop;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;
import ru.itis.master.party.dormdeals.models.jpa.order.OrderMessage;
import ru.itis.master.party.dormdeals.models.jpa.order.OrderProduct;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final UserConverter userConverter;

    public OrderDto from(Order order, Page<OrderMessage> orderMessages) {
        return OrderDto.builder()
                .id(order.getId())
                .customer(userConverter.from(order.getCustomer()))
                .shop(from(order.getShop()))
                .orderProducts(productsFrom(order.getProducts()))
                .orderMessages(messagesFrom(orderMessages))
                .addedDate(order.getAddedDate())
                .price(order.getPrice())
                .build();
    }

    private List<OrderProductDto> productsFrom(List<OrderProduct> products) {
        return products.stream().map(orderProduct -> {
            ProductDto productDto = ProductDto.builder()
                    .id(orderProduct.getProduct().getId())
                    .name(orderProduct.getProduct().getName())
                    .build();
            return OrderProductDto.builder()
                    .productDto(productDto)
                    .count(orderProduct.getCount())
                    .price(orderProduct.getPrice())
                    .build();
        }).toList();
    }

    public Page<OrderDto> fromForCustomer(Page<Order> orders) {
        return orders.map(order -> OrderDto.builder()
                .id(order.getId())
                .shop(from(order.getShop()))
                .addedDate(order.getAddedDate())
                .price(order.getPrice())
                .build());
    }

    public Page<OrderDto> fromForSeller(Page<Order> orders) {
        return orders.map(order -> OrderDto.builder()
                .id(order.getId())
                .customer(from(order.getCustomer()))
                .addedDate(order.getAddedDate())
                .price(order.getPrice())
                .build());
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

    private Page<OrderMessageDto> messagesFrom(Page<OrderMessage> orderMessages) {
        return orderMessages
                .map(orderMessage -> OrderMessageDto.builder()
                        .addedDate(orderMessage.getAddedDate())
                        .userId(orderMessage.getUser().getId())
                        .message(orderMessage.getMessage())
                        .build());
    }
}

package ru.itis.master.party.dormdeals.dto.OrderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Заказ")
public class OrderDto {
    @Schema(description = "Заказчик")
    private User user;

    @Schema(description = "Время заказа", example = "16-03-2023 21:40")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh-mm")
    private Date orderTime;

    @Schema(description = "Комментарий от заказчика", example = "Хочу оставить вам чаевые =)")
    private String userComment;

    @Schema(description = "Магазин")
    private Shop shop;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .user(order.getUser())
                .orderTime(order.getOrderTime())
                .userComment(order.getUserComment())
                .shop(order.getShop())
                .build();
    }

    public static List<OrderDto> from(List<Order> orders) {
        return orders
                .stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

}

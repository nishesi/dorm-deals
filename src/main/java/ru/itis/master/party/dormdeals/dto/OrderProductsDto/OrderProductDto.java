package ru.itis.master.party.dormdeals.dto.OrderProductsDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.models.order.OrderProduct;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Продукты заказа")
public class OrderProductDto {
    @Schema(description = "Заказ")
    private Order order;
    @Schema(description = "Товар")
    private Product product;
    @Schema(description = "Количество", example = "5")
    private Integer count;

    public static OrderProductDto from(OrderProduct orderProduct) {
        return OrderProductDto.builder()
                .order(orderProduct.getOrder())
                .product(orderProduct.getProduct())
                .count(orderProduct.getCount())
                .build();
    }

    public static List<OrderProductDto> from(List<OrderProduct> orderProducts) {
        return orderProducts
                .stream()
                .map(OrderProductDto::from)
                .collect(Collectors.toList());
    }
}

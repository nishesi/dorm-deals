package ru.itis.master.party.dormdeals.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "заказ")
public class NewOrderDto {
    @NotEmpty
    @Valid
    @Schema(description = "список заказываемых сущностей")
    private List<OrderProduct> products;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "элемент заказа")
    public static class OrderProduct {
        @Positive
        @NotNull
        @Schema(description = "айди товара")
        private Long productId;
        @Positive
        @NotNull
        @Schema(description = "количество товаров")
        private Integer count;
    }
}

package ru.itis.master.party.dormdeals.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "заказ")
public record NewOrderForm(
        @NotEmpty
        @Valid
        @Schema(description = "список заказываемых сущностей")
        List<Product> products
) {
    @Schema(description = "элемент заказа")
    public record Product(
            @Positive
            @NotNull
            @Schema(description = "айди товара")
            Long id,

            @Positive
            @NotNull
            @Schema(description = "количество товаров")
            Integer count
    ) {
    }
}

package ru.itis.master.party.dormdeals.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Корзина из куки")
public class CartCookie {
    @Schema(description = "Идентификатор товара из корзины в куки", example = "1")
    private Long id;
    @Schema(description = "Количество товара из корзины в куки", example = "10")
    private int count;
    @Schema(description = "Состояние товара в корзине из куки", example = "true")
    private Boolean stateProduct;
}

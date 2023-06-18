package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Cart;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Товар")
public class CartProductDto {

    @Schema(description = "идентификатор товара", example = "1")
    private Long id;

    @Schema(description = "название товара", example = "Adrenaline Rush")
    private String name;

    @Schema(description = "цена товара", example = "100")
    private float price;

    @Schema(description = "количество в корзине", example = "100")
    private Integer count;

    @Schema(description = "количество на складе", example = "13")
    private short countInStorage;
    @Schema(description = "состояние товара в корзине", example = "ACTIVE")
    private Cart.State state;

    @Schema(description = "url фото товара", example = "http://resource/161346356")
    private String coverImageUrl;
}
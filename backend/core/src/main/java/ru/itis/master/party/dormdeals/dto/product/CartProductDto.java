package ru.itis.master.party.dormdeals.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.jpa.CartProduct;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Товар")
public class CartProductDto {

    @Schema(description = "информация о товаре")
    private Product product;

    @Schema(description = "количество в корзине", example = "100")
    private int count;

    @Schema(description = "состояние товара в корзине", example = "ACTIVE")
    private CartProduct.State state;

    @Schema(description = "изображение товара в корзине")
    private String coverImage;

    public record Product(
            Long id,
            String name,
            Float price,
            Integer countInStorage
    ) {
    }
}
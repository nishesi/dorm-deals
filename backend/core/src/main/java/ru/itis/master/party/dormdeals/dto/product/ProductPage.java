package ru.itis.master.party.dormdeals.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Page;
import ru.itis.master.party.dormdeals.models.jpa.Product.State;

import java.util.List;

public record ProductPage(
        Page<Product> page
) {
    @Builder
    public record Product(
            @Schema(description = "идентификатор товара", example = "1")
            Long id,

            @Schema(description = "название товара", example = "Adrenaline Rush")
            String name,

            @Schema(description = "описание товара", example = "бодрит")
            String description,

            @Schema(description = "категория товара", example = "продукты/напитки")
            String type,

            @Schema(description = "цена товара", example = "100")
            float price,

            @Schema(description = "количество на складе", example = "13")
            int countInStorage,

            @Schema(description = "статус товара")
            State state,

            @Schema(description = "фотографии/видео")
            List<String> resources
    ) {
    }
}

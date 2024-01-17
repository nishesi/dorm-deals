package ru.itis.master.party.dormdeals.dto.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static ru.itis.master.party.dormdeals.models.jpa.Product.State;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopWithProductsDto {

    private Shop shop;

    private Page<Product> productsPage;

    public record Shop(
            Long id,
            String name
    ) {
    }

    @Builder
    public record Product(
            @Schema(description = "идентификатор товара", example = "1")
            Long id,

            @Schema(description = "название товара", example = "Adrenaline Rush")
            String name,

            @Schema(description = "описание товара", example = "бодрит")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String description,

            @Schema(description = "категория товара", example = "продукты/напитки")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String type,

            @Schema(description = "цена товара", example = "100")
            float price,

            @Schema(description = "количество на складе", example = "13")
            int countInStorage,

            @Schema(description = "статус товара")
            State state,

            @Schema(description = "фотографии/видео")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            List<String> resources
    ) {
    }
}

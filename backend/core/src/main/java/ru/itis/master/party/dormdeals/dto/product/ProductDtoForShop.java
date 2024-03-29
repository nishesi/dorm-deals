package ru.itis.master.party.dormdeals.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoForShop {
    @Schema(description = "идентификатор товара", example = "1")
    private Long id;

    @Schema(description = "название товара", example = "Adrenaline Rush")
    private String name;

    @Schema(description = "описание товара", example = "бодрит")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Schema(description = "категория товара", example = "продукты/напитки")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;

    @Schema(description = "цена товара", example = "100")
    private float price;

    @Schema(description = "количество на складе", example = "13")
    private int countInStorage;

    @Schema(description = "статус товара")
    private Product.State state;

    @Schema(description = "фотографии/видео")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> resources;
}

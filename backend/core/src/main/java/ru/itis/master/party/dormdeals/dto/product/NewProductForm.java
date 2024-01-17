package ru.itis.master.party.dormdeals.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Новый товар")
public record NewProductForm(
        @Schema(description = "название товара", example = "Adrenaline Rush")
        @NotBlank(message = "{constraint.field.not-blank.message}")
        @Size(max = 50, message = "{constraint.field.max.message}")
        String name,

        @Schema(description = "описание товара", example = "бодрит")
        @NotBlank
        @Size(max = 1000, message = "{constraint.field.max.message}")
        String description,

        @Schema(description = "категория товара", example = "продукты/напитки")
        @NotBlank(message = "{constraint.field.not-blank.message}")
        String type,

        @Schema(description = "цена товара", example = "100")
        @NotNull
        @Max(99999)
        @PositiveOrZero
        Float price,

        @Schema(description = "количество на складе", example = "13")
        @NotNull
        @Max(32000)
        @PositiveOrZero
        Short countInStorage
) {
}

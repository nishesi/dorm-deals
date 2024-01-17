package ru.itis.master.party.dormdeals.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Обновление товара")
public record UpdateProductForm(
        @Schema(description = "название товара", example = "Adrenaline Rush")
        @NotBlank(message = "Поле обязательно к заполнению")
        @Size(max = 50, message = "Слишком длинное или короткое название")
        String name,

        @Schema(description = "описание товара", example = "бодрит")
        @NotBlank
        @Size(max = 1000, message = "Слишком длинное или короткое описание")
        String description,

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

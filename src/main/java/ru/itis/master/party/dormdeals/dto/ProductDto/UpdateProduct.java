package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Обновление товара")
public class UpdateProduct {

    @Schema(description = "название товара", example = "Adrenaline Rush")
    @NotBlank(message = "Поле обязательно к заполнению")
    @Size(min = 2, max = 50, message = "Слишком длинное или короткое название")
    private String name;

    @Schema(description = "описание товара", example = "бодрит")
    @Size(min = 1, max = 1000, message = "Слишком длинное или короткое описание")
    private String description;

    @Schema(description = "цена товара", example = "100")
    @Max(99999)
    @PositiveOrZero
    private Float price;

    @Schema(description = "количество на складе", example = "13")
    @Max(32000)
    @PositiveOrZero
    private Short countInStorage;
}

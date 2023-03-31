package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Новый товар")
public class NewProduct {

    @Schema(description = "название товара", example = "Adrenaline Rush")
    @NotBlank(message = "Поле обязательно к заполнению")
    @Size(max = 50, message = "Слишком длинное или короткое название")
    private String name;

    @Schema(description = "описание товара", example = "бодрит")
    @NotBlank
    @Size(max = 1000, message = "Слишком длинное или короткое описание")
    private String description;

    @Schema(description = "категория товара", example = "продукты/напитки")
    @NotBlank
    private String category;

    @Schema(description = "цена товара", example = "100")
    @NotNull
    @Max(99999)
    @PositiveOrZero
    private Float price;

    @Schema(description = "количество на складе", example = "13")
    @NotNull
    @Max(32000)
    @PositiveOrZero
    private Short countInStorage;
}

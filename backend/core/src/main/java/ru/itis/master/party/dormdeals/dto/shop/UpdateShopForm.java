package ru.itis.master.party.dormdeals.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Обновленный магазин")
public record UpdateShopForm(
        @Schema(description = "название магазина", example = "казанэкспресс")
        @NotBlank
        @Size(min = 5, max = 100)
        String name,

        @Schema(description = "описание магазина", example = "у нас самые низкие цены")
        @NotBlank
        @Size(max = 1000)
        String description,

        @Schema(description = "идентификаторы мест продажи", example = "['1', '2', '3']")
        @NotEmpty(message = "{constraint.dormitories.nonempty.message}")
        List<Long> dormitoryIdList
) {
}

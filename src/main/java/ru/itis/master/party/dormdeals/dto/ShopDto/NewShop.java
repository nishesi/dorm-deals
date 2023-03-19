package ru.itis.master.party.dormdeals.dto.ShopDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Новый магазин")
public class NewShop {

    @Schema(description = "название магазина", example = "казанэкспресс")
    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    @Schema(description = "описание магазина", example = "у нас самые низкие цены")
    @NotBlank
    @Size(max = 1000)
    private String description;

    @Schema(description = "идентификаторы мест продажи", example = "[1, 2, 3]")
    private List<Long> dormitoryIdList;
}

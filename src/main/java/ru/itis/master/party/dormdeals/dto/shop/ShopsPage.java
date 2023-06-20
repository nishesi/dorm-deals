package ru.itis.master.party.dormdeals.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Страница с магазинами и общее количество страниц")
public class ShopsPage {

    @Schema(description = "список магазинов")
    private List<ShopDto> shops;

    @Schema(description = "общее количество страниц", example = "5")
    private Integer totalPagesCount;
}


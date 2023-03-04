package ru.itis.master.party.dormdeals.dto.ProductDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductsPage {
    @Schema(description = "Страница с товарами и общее количество страниц")
    private List<ProductDto> products;
    @Schema(description = "Общее количество страниц", example = "5")
    private Integer totalPageCount;
}

package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopWithProducts {
    private ShopDto shop;
    private ProductsPage productsPage;
}

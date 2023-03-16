package ru.itis.master.party.dormdeals.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.models.Shop;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopWithProducts {
    private Shop shop;
    private ProductsPage productsPage;
}

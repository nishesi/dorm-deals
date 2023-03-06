package ru.itis.master.party.dormdeals.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopWithProducts {
    private Shop shop;
    private ProductsPage productsPage;
}

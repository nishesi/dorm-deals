package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ProductDto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ProductDto.UpdateProduct;

public interface ProductService {
    ProductsPage getAllProducts(int page);

    ProductDto addProduct(long userId, NewProduct newProduct);

    ProductDto getProduct(Long productId);

    ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct);

    void deleteProduct(long userId, Long productId);

//    ProductsPage getAllProductsByShop(int page, Long shopId);

    void returnInSell(long userId, Long productId);
}

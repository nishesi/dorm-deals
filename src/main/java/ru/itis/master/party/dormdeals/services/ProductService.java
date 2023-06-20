package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ProductDto.*;

import java.util.List;

public interface ProductService {
    ProductsPage getAllProducts(int page);

    ProductDto addProduct(long userId, NewProduct newProduct);

    ProductDto getProduct(Long productId);

    ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct);

    void deleteProduct(long userId, Long productId);

//    ProductsPage getAllProductsByShop(int page, Long shopId);

    void returnInSell(long userId, Long productId);
    List<CartProductDto> getCartProducts(List<Long> productsId);
}

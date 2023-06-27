package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.product.*;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;

public interface ProductService {

    ProductDto addProduct(long userId, NewProduct newProduct);

    ProductDto getProduct(long userId, Long productId);

    ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct);

    void updateProductState(long userId, long productId, Product.State state);
    List<CartProductDto> getCartProducts(List<Long> productsId);
}

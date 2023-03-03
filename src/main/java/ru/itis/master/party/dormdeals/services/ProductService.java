package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.UpdateProduct;

public interface ProductService {
    ProductsPage getAllProducts(int page);

    ProductDto addProduct(NewProduct newProduct);

    ProductDto getProduct(Long productId);

    ProductDto updateProduct(Long productId, UpdateProduct updatedProduct);

    void deleteProduct(Long productId);
}

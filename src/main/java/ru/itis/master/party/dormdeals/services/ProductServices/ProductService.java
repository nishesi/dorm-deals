package ru.itis.master.party.dormdeals.services.ProductServices;

import ru.itis.master.party.dormdeals.dto.ProductDto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ProductDto.UpdateProduct;

public interface ProductService {
    ProductsPage getAllProducts(int page);

    ProductDto addProduct(NewProduct newProduct);

    ProductDto getProduct(Long productId);

    ProductDto updateProduct(Long productId, UpdateProduct updatedProduct);

    void deleteProduct(Long productId);
}

package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.product.*;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;

public interface ProductService {

    ProductDto addProduct(long userId, NewProduct newProduct);

    ProductDto getProduct(Long productId, Long userId, int pageIndex);

    ProductDto updateProduct(long userId, Long productId, UpdateProduct updatedProduct);

    void updateProductState(long userId, long productId, Product.State state);
    List<CartProductDto> getCartProducts(List<Long> productsId);

    void addProductImage(long userId, Long productId, MultipartFile productImage);

    void deleteProductImage(long userId, Long productId, String imageId);
}

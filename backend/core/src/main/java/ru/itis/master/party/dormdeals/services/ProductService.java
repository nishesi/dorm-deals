package ru.itis.master.party.dormdeals.services;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.product.*;
import ru.itis.master.party.dormdeals.models.jpa.Product;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(long userId, NewProductForm form);

    ProductDto getProduct(Long productId, Long userId, int pageIndex);

    ProductDto updateProduct(long userId, Long productId, UpdateProductForm form);

    void updateProductState(long userId, long productId, Product.State state);

    List<CartProductDto> getCartProducts(List<Long> productsId);

    void addProductImage(long userId, Long productId, MultipartFile productImage);

    void deleteProductImage(long userId, Long productId, String imageId);

    ProductPage getShopsActiveProductsPage(Long shopId, Pageable pageable);
}

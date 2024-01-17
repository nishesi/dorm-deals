package ru.itis.master.party.dormdeals.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.ProductApi;
import ru.itis.master.party.dormdeals.dto.product.NewProductForm;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.UpdateProductForm;
import ru.itis.master.party.dormdeals.models.jpa.Product;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> addProduct(@Valid NewProductForm newProductForm,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        ProductDto productDto = productService.createProduct(userId, newProductForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long productId, int pageIndex,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            long userId = userDetails.getUser().getId();
            return ResponseEntity.ok(productService.getProduct(productId, userId, pageIndex));
        }

        return ResponseEntity.ok(productService.getProduct(productId, null, pageIndex));
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long productId,
                                                    @Valid UpdateProductForm updatedProduct,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.accepted().body(productService.updateProduct(userId, productId, updatedProduct));
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        productService.updateProductState(userId, productId, Product.State.DELETED);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> updateProductState(Long productId,
                                                Product.State state,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.updateProductState(userDetails.getUser().getId(), productId, state);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> addProductImage(Long productId,
                                             MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.addProductImage(userDetails.getUser().getId(), productId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<?> deleteProductImage(Long productId,
                                                String imageId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProductImage(userDetails.getUser().getId(), productId, imageId);
        return ResponseEntity.accepted().build();
    }
}

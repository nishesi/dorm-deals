package ru.itis.master.party.dormdeals.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ProductApi;
import ru.itis.master.party.dormdeals.dto.product.NewProduct;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.UpdateProduct;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> addProduct(@Valid NewProduct newProduct,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        ProductDto productDto = productService.addProduct(userId, newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long productId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(productService.getProduct(userDetails.getUser().getId(), productId));
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long productId,
                                                    @Valid UpdateProduct updatedProduct,
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
}

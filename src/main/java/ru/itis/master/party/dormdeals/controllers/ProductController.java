package ru.itis.master.party.dormdeals.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.ProductApi;
import ru.itis.master.party.dormdeals.dto.product.NewProduct;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductsPage;
import ru.itis.master.party.dormdeals.dto.product.UpdateProduct;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

//    @Override
//    public ResponseEntity<ProductsPage> getAllProductsByShop(int page, Long shopId) {
//        return ResponseEntity.ok(productService.getAllProductsByShop(page, shopId));
//    }

    //TODO сделать page Long и required = false и при пустом значении возвращать первую страницу
    @Override
    public ResponseEntity<ProductsPage> getAllProducts(int page) {
        return ResponseEntity.ok(productService.getAllProducts(page));
    }

    @Override
    public ResponseEntity<ProductDto> addProduct(@Valid NewProduct newProduct,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(userId, newProduct));
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long productId, @Valid UpdateProduct updatedProduct,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.accepted().body(productService.updateProduct(userId, productId, updatedProduct));
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        productService.deleteProduct(userId, productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<ProductDto> returnProductInSell(Long productId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        productService.returnInSell(userId, productId);
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

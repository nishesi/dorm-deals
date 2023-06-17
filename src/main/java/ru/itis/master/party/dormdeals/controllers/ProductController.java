package ru.itis.master.party.dormdeals.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ProductApi;
import ru.itis.master.party.dormdeals.dto.ProductDto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ProductDto.UpdateProduct;
import ru.itis.master.party.dormdeals.services.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

//    @Override
//    public ResponseEntity<ProductsPage> getAllProductsByShop(int page, Long shopId) {
//        return ResponseEntity.ok(productService.getAllProductsByShop(page, shopId));
//    }

    @Override
    public ResponseEntity<ProductsPage> getAllProducts(int page) {
        return ResponseEntity.ok(productService.getAllProducts(page));
    }

    @Override
    public ResponseEntity<ProductDto> addProduct(@Valid NewProduct newProduct) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(newProduct));
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long productId, @Valid UpdateProduct updatedProduct) {
        return ResponseEntity.accepted().body(productService.updateProduct(productId, updatedProduct));
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<ProductDto> returnProductInSell(Long productId) {
        productService.returnInSell(productId);
        return ResponseEntity.accepted().build();
    }
}

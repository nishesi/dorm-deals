package ru.itis.master.party.dormdeals.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.ShopApi;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.product.ProductPage;
import ru.itis.master.party.dormdeals.dto.shop.NewShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShopForm;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.OrderService;
import ru.itis.master.party.dormdeals.services.ProductService;
import ru.itis.master.party.dormdeals.services.ShopService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
public class ShopController implements ShopApi {

    private final ShopService shopService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ExecutorService executorService;


    @Override
    public ResponseEntity<ShopsMainPage> getShopsMainPage(Long shopId,
                                                          Pageable pageable
    ) throws ExecutionException, InterruptedException {
        Future<ShopDto> shop = executorService
                .submit(() -> shopService.getShop(shopId));
        Future<ProductPage> products = executorService
                .submit(() -> productService.getShopsActiveProductsPage(shopId, pageable));

        return ResponseEntity.ok(new ShopsMainPage(shop.get(), products.get()));
    }

    @Override
    public ResponseEntity<ShopDto> createShop(@Valid NewShopForm newShopForm,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopService.createShop(userId, newShopForm));
    }

    @Override
    public ResponseEntity<ShopDto> updateShop(Long shopId, @Valid UpdateShopForm updateShopForm,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.accepted()
                .body(shopService.updateShop(userId, shopId, updateShopForm));
    }

    @Override
    public ResponseEntity<Void> deleteShop(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        shopService.deleteShop(userDetails.getUser().getId());
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Page<OrderDto>> getShopOrders(Long shopId,
                                                        Pageable pageable,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<OrderDto> shopOrders = orderService
                .getShopOrders(userDetails.getUser().getId(), shopId, pageable);
        return ResponseEntity.ok(shopOrders);
    }

    @Override
    public ResponseEntity<?> updateShopImage(MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        shopService.updateShopImage(userDetails.getUser().getId(), file);
        return ResponseEntity.accepted().build();
    }
}

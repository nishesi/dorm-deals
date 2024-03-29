package ru.itis.master.party.dormdeals.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.ShopApi;
import ru.itis.master.party.dormdeals.dto.shop.NewShop;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.OrderService;
import ru.itis.master.party.dormdeals.services.ShopService;

@RestController
@RequiredArgsConstructor
public class ShopController implements ShopApi {

    private final ShopService shopService;
    private final OrderService orderService;


    @Override
    public ResponseEntity<ShopWithProducts> getMainPageShop(Long shopId, int pageIndex) {
        return ResponseEntity.ok()
                .body(shopService.getAllProductsThisShop(shopId, pageIndex));
    }

    @Override
    public ResponseEntity<ShopDto> createShop(@Valid NewShop newShop,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopService.createShop(userId, newShop));
    }

    @Override
    public ResponseEntity<ShopDto> updateShop(Long shopId, @Valid UpdateShop updateShop,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.accepted()
                .body(shopService.updateShop(userId, shopId, updateShop));
    }

    @Override
    public ResponseEntity<?> deleteShop(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        shopService.deleteShop(userDetails.getUser().getId());
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Page<OrderDto>> getShopOrders(Long shopId, Integer pageInd, Integer pageSize,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(pageInd, pageSize);
        Page<OrderDto> shopOrders = orderService.getShopOrders(userDetails.getUser().getId(), shopId, pageable);
        return ResponseEntity.ok(shopOrders);
    }

    @Override
    public ResponseEntity<?> updateShopImage(MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        shopService.updateShopImage(userDetails.getUser().getId(), file);
        return ResponseEntity.accepted().build();
    }
}

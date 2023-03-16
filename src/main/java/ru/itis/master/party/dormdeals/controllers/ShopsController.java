package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ShopsApi;
import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;
import ru.itis.master.party.dormdeals.services.ShopsService;

@RestController
@RequiredArgsConstructor
public class ShopsController implements ShopsApi {
    private final ShopsService shopsService;

    @Override
    public ResponseEntity<ShopsPage> getAllShops(int page) {
        return ResponseEntity
                .ok(shopsService.getAllShops(page));
    }

    @Override
    public ResponseEntity<ShopDto> createShop(NewShop newShop) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopsService.createShop(newShop));
    }

    @Override
    public ResponseEntity<ShopDto> getShop(Long shopId) {
        return ResponseEntity.ok(shopsService.getShop(shopId));
    }

    @Override
    public ResponseEntity<ShopDto> updateShop(Long shopId, UpdateShop updateShop) {
        return ResponseEntity.accepted().body(shopsService.updateShop(shopId, updateShop));
    }

    @Override
    public ResponseEntity<?> deleteShop(Long shopId) {
        shopsService.deleteShop(shopId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<ShopWithProducts> getAllProductsThisShop(Long shopId, int page) {
        return ResponseEntity.ok().body(shopsService.getAllProductsThisShop(shopId, page));
    }


}

package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.ShopsApi;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.services.ShopServices.ShopsService;

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
    public ResponseEntity<ShopDto> createShop(ShopDto newShop, Long ownerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopsService.createShop(newShop, ownerId));
    }

    @Override
    public ResponseEntity<ShopDto> getShop(Long shopId) {
        return ResponseEntity.ok(shopsService.getShop(shopId));
    }

    @Override
    public ResponseEntity<ShopDto> updateShop(Long shopId, ShopDto updatedShop) {
        return ResponseEntity.accepted().body(shopsService.updateShop(shopId, updatedShop));
    }

    @Override
    public ResponseEntity<?> deleteShop(Long shopId) {
        shopsService.deleteShop(shopId);
        return ResponseEntity.accepted().build();
    }
}

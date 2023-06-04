package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
//import ru.itis.master.party.dormdeals.dto.FavouritesDto;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.services.FavoriteService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PersonalUserController implements PersonalUserControllerApi {
    private final FavoriteService favoriteService;
    private final CartService cartService;

    @Override
    public ResponseEntity<?> addFavoriteProduct(Long productId) {
        favoriteService.addFavorite(productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getFavorites() {
        return ResponseEntity.ok(favoriteService.getFavorites());
    }

    @Override
    public ResponseEntity<?> deleteFavoriteProduct(Long productId) {
        favoriteService.deleteFavorite(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> addCartProduct(Long productId) {
        if (checkAuthentication()) {
            cartService.addCart(productId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
            //TODO сделать что то с добавление товара в корзину для неавторизованного юзера
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @Override
    public ResponseEntity<CartDto> getCart(String cookieHeader) {
        return ResponseEntity.ok(cartService.getCart(cookieHeader));
//        if (checkAuthentication()) {
//            return ResponseEntity.ok(cartService.getCart());
//        //TODO сделать что то с получение корзины для неавторизованного юзера
//        } else {
//            return ResponseEntity.ok().build();
//        }
    }

    @Override
    public ResponseEntity<?> deleteCartProduct(Long productId) {
        cartService.deleteCart(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> setCountProductInCart(Long productId, Integer count) {
        cartService.setCountProduct(productId, count);
        return ResponseEntity.accepted().build();
    }

    private Boolean checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

}

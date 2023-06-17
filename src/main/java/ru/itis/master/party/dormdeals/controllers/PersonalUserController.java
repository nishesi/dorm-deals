package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.services.FavoriteService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PersonalUserController implements PersonalUserControllerApi {
    private final FavoriteService favoriteService;
    private final CartService cartService;

    @Override
    public ResponseEntity<?> addFavouriteProduct(Long productId) {
        favoriteService.addFavorite(productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getFavorites() {
        return ResponseEntity.ok(favoriteService.getFavorites());
    }

    @Override
    public ResponseEntity<?> deleteFavouriteProduct(Long productId) {
        favoriteService.deleteFavorite(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> addCartProduct(Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            cartService.addCart(userDetails.getUser().getId(), productId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            //TODO сделать что то с добавление товара в корзину для неавторизованного юзера
            throw new RuntimeException("not realized");
        }
    }

    @Override
    public ResponseEntity<CartDto> getCart(String cookieHeader,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartDto carts = userDetails != null
                ? cartService.getCart(userDetails.getUser().getId(), cookieHeader)
                : cartService.getCart(cookieHeader);
        return ResponseEntity.ok(carts);
    }

    @Override
    public ResponseEntity<?> deleteCartProduct(Long productId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            long userId = userDetails.getUser().getId();
            cartService.deleteCart(userId, productId);
            return ResponseEntity.accepted().build();
        }
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResponseEntity<?> setCountProductInCart(Long productId, Integer count,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            long userId = userDetails.getUser().getId();
            cartService.setCountProduct(userId, productId, count);
            return ResponseEntity.accepted().build();
        }
        throw new RuntimeException("Not implemented");
    }
}

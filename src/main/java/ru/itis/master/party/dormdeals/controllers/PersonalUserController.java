package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
import ru.itis.master.party.dormdeals.dto.CartCookie;
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
    public ResponseEntity<?> addFavoriteProduct(Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(favoriteService.getFavorites(userId));
    }

    @Override
    public ResponseEntity<?> deleteFavoriteProduct(Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        favoriteService.deleteFavorite(userId, productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> addCartProduct(Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            cartService.addCart(userDetails.getUser().getId(), productId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            throw new RuntimeException("not realized");
        }
    }

    @Override
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           List<Long> productsId) {
        CartDto carts = userDetails != null
                ? cartService.getCart(userDetails.getUser().getId(), productsId)
                : cartService.getCart(productsId);
        return ResponseEntity.ok(carts);
    }


    @Override
    public ResponseEntity<?> cartSynchronization(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 List<CartCookie> cartsCookie) {

        cartService.cartSynchronization(userDetails.getUser().getId(), cartsCookie);

        return ResponseEntity.accepted().build();
    }
}

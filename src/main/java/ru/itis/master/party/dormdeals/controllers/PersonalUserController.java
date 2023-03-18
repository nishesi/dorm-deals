package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
//import ru.itis.master.party.dormdeals.dto.FavouritesDto;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.services.FavouriteService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PersonalUserController implements PersonalUserControllerApi {
    private final FavouriteService favouriteService;
    private final CartService cartService;

    @Override
    public ResponseEntity<?> addFavouriteProduct(Long productId) {
        favouriteService.addFavourite(productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getFavourites() {
        return ResponseEntity.ok(favouriteService.getFavourites());
    }

    @Override
    public ResponseEntity<?> deleteFavouriteProduct(Long productId) {
        favouriteService.deleteFavourite(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<?> addCartProduct(Long productId) {
        cartService.addCart(productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }
}

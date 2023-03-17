package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
//import ru.itis.master.party.dormdeals.dto.FavouritesDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.services.FavouriteService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PersonalUserController implements PersonalUserControllerApi {
    private final FavouriteService favouriteService;

    @Override
    public ResponseEntity<?> addProduct(Long productId) {
        favouriteService.addFavourite(productId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getFavourites() {
        return ResponseEntity.ok(favouriteService.getFavourites());
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        favouriteService.deleteFavourite(productId);
        return ResponseEntity.accepted().build();
    }
}

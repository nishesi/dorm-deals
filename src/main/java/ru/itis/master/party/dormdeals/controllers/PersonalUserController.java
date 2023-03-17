package ru.itis.master.party.dormdeals.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.PersonalUserControllerApi;
import ru.itis.master.party.dormdeals.services.FavouriteService;


@RestController
@RequiredArgsConstructor
public class PersonalUserController implements PersonalUserControllerApi {
    private final FavouriteService favouriteService;

    @Override
    public ResponseEntity<?> addProduct(Long productId) {
        favouriteService.addFavourite(productId);
        return ResponseEntity.ok().build();
    }
}

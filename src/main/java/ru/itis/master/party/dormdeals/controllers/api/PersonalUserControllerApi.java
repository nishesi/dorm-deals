package ru.itis.master.party.dormdeals.controllers.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/my")
public interface PersonalUserControllerApi {
    @PutMapping("/favourites/{product-id}")
    ResponseEntity<?> addProduct(@PathVariable("product-id") Long productId);

}

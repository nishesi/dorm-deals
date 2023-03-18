package ru.itis.master.party.dormdeals.controllers.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

import java.util.List;

@RequestMapping("/my")
public interface PersonalUserControllerApi {
    @Operation(summary = "Добавление товара в избранное")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар добавлен в избранное"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/favourites/{product-id}")
    ResponseEntity<?> addFavouriteProduct(@Parameter(name = "Идентификатор товара") @PathVariable("product-id") Long productId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Избранное",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))
                    })
    })
    @GetMapping("/favourites")
    ResponseEntity<List<ProductDto>> getFavourites();

    @Operation(summary = "Удаление товара из избранное")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар удален из избранного"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })

    @DeleteMapping("/favourites/{product-id}")
    ResponseEntity<?> deleteFavouriteProduct(@Parameter(name = "Идентификатор товара") @PathVariable("product-id") Long productId);


    @Operation(summary = "Добавление товара в корзину")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар добавлен в корзину"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/cart/{product-id}")
    ResponseEntity<?> addCartProduct(@Parameter(name = "Индентификатор товара") @PathVariable("product-id") Long productId);


    @GetMapping("/cart")
    ResponseEntity<CartDto> getCart();

//    @PutMapping("/cart/{product-id}/inactive")
//    ResponseEntity<?> inactiveProduct(@Parameter(name = "Индентификатор товара") @PathVariable("product-id") Long productId);
}

package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    ResponseEntity<?> addFavoriteProduct(
            @Parameter(name = "Идентификатор товара")
            @PathVariable("product-id")
            Long productId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Избранное",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            )
    })
    @GetMapping("/favourites")
    ResponseEntity<List<ProductDto>> getFavorites();

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
    ResponseEntity<?> deleteFavoriteProduct(
            @Parameter(name = "Идентификатор товара")
            @PathVariable("product-id")
            Long productId);

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
    ResponseEntity<?> addCartProduct(
            @Parameter(name = "Индентификатор товара")
            @PathVariable("product-id")
            Long productId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Корзина",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartDto.class))
                    }
            )
    })
    @GetMapping("/cart")
    ResponseEntity<CartDto> getCart(
            @RequestHeader(value = "Cookie", required = false)
            String cookieHeader);

//    @PutMapping("/cart/{product-id}/inactive")
//    ResponseEntity<?> inactiveProduct(@Parameter(name = "Индентификатор товара") @PathVariable("product-id") Long productId);

    @Operation(summary = "Удаление товара из корзины")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Товар удален из корзины"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/cart/{product-id}")
    ResponseEntity<?> deleteCartProduct(
            @Parameter(name = "Индентификатор товара")
            @PathVariable("product-id")
            Long productId);

    @Operation(summary = "Обновление количества товара в корзине")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Количество обновлено"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/cart/{product-id}/{count}")
    ResponseEntity<?> setCountProductInCart(
            @Parameter(name = "Индентификатор товара")
            @PathVariable("product-id")
            Long productId,
            @Parameter(description = "Количество товара в корзине")
            @PathVariable("count")
            Integer count);
}

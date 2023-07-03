package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;

import java.util.List;

@RequestMapping("/my")
@Tags(value = {
        @Tag(name = "Personal User Logic")
})
public interface PersonalUserApi {

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
    @PutMapping("/favorites/{product-id}")
    ResponseEntity<?> addFavoriteProduct(
            @Parameter(description = "Идентификатор продукта")
            @PathVariable("product-id")
            Long productId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Избранное",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            )
    })
    @GetMapping("/favorites")
    ResponseEntity<List<ProductDto>> getFavorites(
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

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
    @DeleteMapping("/favorites/{product-id}")
    ResponseEntity<?> deleteFavoriteProduct(
            @Parameter(description = "Идентификатор товара")
            @PathVariable("product-id")
            Long productId,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Корзина",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartProductDto.class))
                    }
            )
    })
    @GetMapping("/cart")
    ResponseEntity<List<CartProductDto>> getCart(
            @Parameter(hidden = true)
            UserDetailsImpl userDetails,
            @RequestParam(value = "id", required = false) List<Long> productsId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Корзина синхронизирована"),
            //TODO: сделать ошибку и сюда ее бахнуть
            @ApiResponse(responseCode = "500", description = "Товара нет на складе")
    })
    @PostMapping("/cart/synchronization")
    ResponseEntity<?> cartSynchronization(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @Parameter(description = "Список товаров из куки") @RequestBody List<CartCookie> cartsCookies);

}

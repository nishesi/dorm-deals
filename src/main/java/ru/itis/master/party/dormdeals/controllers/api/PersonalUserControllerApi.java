package ru.itis.master.party.dormdeals.controllers.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

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
    ResponseEntity<?> addProduct(@Parameter(name = "Идентификатор товара") @PathVariable("product-id") Long productId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Избранное",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))
                    })
    })
    @GetMapping("/favourites")
    ResponseEntity<ProductDto> getFavourites();
}

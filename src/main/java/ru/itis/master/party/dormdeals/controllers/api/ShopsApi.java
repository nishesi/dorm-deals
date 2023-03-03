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
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopsPage;

@Tags(value = {
        @Tag(name = "Shops")
})
@RequestMapping("/shops")
public interface ShopsApi {

    @Operation(summary = "Получение списка магазинов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с магазинами",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ShopsPage.class))
                    })
    })
    @GetMapping
    ResponseEntity<ShopsPage> getAllShops(@Parameter(description = "Номер страницы") @RequestParam("page") int page);

    @Operation(summary = "Добавление магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный магазин",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ShopDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<ShopDto> addShop(@RequestBody ShopDto newShop);

    @Operation(summary = "Получение магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о магазине",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ShopDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{shop-id}")
    ResponseEntity<ShopDto> getShop(@Parameter(description = "Идентификатор магазина", example = "1")
                                        @PathVariable("shop-id") Long shopId);

    @Operation(summary = "Обновление магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный магазин",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ShopDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{shop-id}")
    ResponseEntity<ShopDto> updateShop(
            @Parameter(description = "Идентификатор магазина", example = "1") @PathVariable("shop-id") Long shopId,
            @RequestBody ShopDto updatedShop);

    @Operation(summary = "Удаление магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Магазин удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{shop-id}")
    ResponseEntity<?> deleteShop(
            @Parameter(description = "Идентификатор магазина", example = "1") @PathVariable("shop-id") Long shopId);
}

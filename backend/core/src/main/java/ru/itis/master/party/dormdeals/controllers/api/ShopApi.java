package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.aspects.RestExceptionHandler.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.shop.NewShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopWithProductsDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShopForm;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

@Tags(value = {
        @Tag(name = "Shops")
})
@RequestMapping("/shops")
public interface ShopApi {


    @Operation(summary = "Получение главной страницы магазина с его товарами постранично")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о магазине и его товары",
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
    ResponseEntity<ShopWithProductsDto> getMainPageShop(
            @Parameter(description = "Идентификатор магазина")
            @PathVariable("shop-id")
            Long shopId,
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
            int pageIndex);

    @Operation(summary = "Добавление магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный магазин",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ShopDto.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<ShopDto> createShop(
            @RequestBody
            NewShopForm newShopForm,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);


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
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PutMapping("/{shop-id}")
    ResponseEntity<ShopDto> updateShop(
            @Parameter(description = "Идентификатор магазина", example = "1")
            @PathVariable("shop-id")
            Long shopId,
            @RequestBody
            UpdateShopForm updateShopForm,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

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
    @DeleteMapping
    ResponseEntity<?> deleteShop(
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Получение списка заказов магазина")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о заказах в магизне получены"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
            content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionDto.class))
            })
    })
    @GetMapping("/{shop-id}/orders")
    ResponseEntity<Page<OrderDto>> getShopOrders(
            @PathVariable("shop-id")
            Long shopId,
            @Parameter(description = "индекс страницы, по умолчанию = 0")
            @RequestParam(required = false, defaultValue = "0")
            Integer pageInd,
            @Parameter(description = "размер страницы, по умолчанию = 10")
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "обновление иконки магазина")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = "multipart/form-data",
            schema = @Schema(type = "object"),
            schemaProperties = {
                    @SchemaProperty(name = "file",
                            schema = @Schema(type = "string", format = "binary"))}
    ))
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "изменено")
    })
    @PostMapping(value = "/image")
    ResponseEntity<?> updateShopImage(
            @Parameter(description = "новая иконка")
            @RequestParam("file")
            MultipartFile file,
            @Parameter(hidden = true) UserDetailsImpl userDetails);
}


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
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderWithProducts;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

import java.util.List;

@Tags(value = {
        @Tag(name = "Orders")
})
@RequestMapping("/orders")
public interface OrderApi {

    @Operation(summary = "Заказ(ы) товаров из корзины")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ(ы) создан(ы)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "Сведения о невалидных данных",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<List<OrderDto>> createOrder(
            @Parameter(description = "Список товаров с корзины")
            @RequestBody
            List<CartProductDto> cartProductDtoList,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Получение заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о заказе",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{order-id}")
    ResponseEntity<OrderDto> getOrder(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId);

    @Operation(summary = "Обновление статуса заказа на CONFIRMED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{order-id}/confirm")
    ResponseEntity<OrderDto> updateOrderStateToConfirmed(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId);

    @Operation(summary = "Обновление статуса заказа на IN_DELIVERY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{order-id}/in_delivery")
    ResponseEntity<OrderDto> updateOrderStateToInDelivery(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId);

    @Operation(summary = "Обновление статуса заказа на DELIVERED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{order-id}/delivered")
    ResponseEntity<OrderDto> updateOrderStateToDelivered(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId);

    @Operation(summary = "Удаление заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Заказ удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{order-id}")
    ResponseEntity<?> deleteOrder(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId);

    @Operation(summary = "Получение заказа с его товарами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о заказе и его товары",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{order-id}/products")
    ResponseEntity<OrderWithProducts> getAllProductsThisOrder(
            @Parameter(description = "Идентификатор заказа")
            @PathVariable("order-id")
            Long orderId);
}
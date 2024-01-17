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
import ru.itis.master.party.dormdeals.aspects.RestExceptionHandler.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.order.NewOrderForm;
import ru.itis.master.party.dormdeals.dto.order.NewOrderMessageForm;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

@Tags(value = {
        @Tag(name = "Orders")
})
public interface OrderApi {

    @Operation(summary = "Заказ товаров из корзины")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ(ы) создан(ы)"),
            @ApiResponse(responseCode = "422", description = "Сведения о невалидных данных",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<?> createOrder(
            @RequestBody
            NewOrderForm newOrderForm,
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

    @Operation(summary = "Обновление статуса заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный заказ"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{order-id}/{state}")
    ResponseEntity<OrderDto> updateOrderState(
            @Parameter(description = "Идентификатор заказа", example = "1")
            @PathVariable("order-id")
            Long orderId,
            @Parameter(description = "новый статус заказа", example = "CONFIRMED")
            @PathVariable
            Order.State state,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "Добавление сообщения к заказу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "сообщение добавлено"),
            @ApiResponse(responseCode = "406", description = "ошибка выполнения",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "заказ не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
    })
    @PostMapping("/{order-id}/message")
    ResponseEntity<?> addMessage(
            @PathVariable("order-id")
            Long orderId,
            @RequestBody
            NewOrderMessageForm newOrderMessageForm,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);
}
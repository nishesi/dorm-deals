package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.MessageDto;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.dto.orders.OrderDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

@Tags(value = {
        @Tag(name = "Users")
})
@RequestMapping("/user")
@Schema(description = "Работа с пользователем")
public interface UserApi {

    @Operation(summary = "регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "user",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDto.class))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "пользователь существует",
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
    @PostMapping
    ResponseEntity<?> addUser(
            @RequestBody
            NewUserDto userDto);

    @Operation(summary = "Получение информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping
    UserDto getUser(
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "обновление информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "user",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "пользователь не найден",
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
    @PutMapping
    UserDto updateUser(
            @RequestBody
            UpdateUserDto userDto,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @Operation(summary = "удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user deleted"),
            @ApiResponse(responseCode = "404", description = "user not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping
    ResponseEntity<?> deleteUser(
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);

    @GetMapping("/orders")
    ResponseEntity<Page<OrderDto>> getUserOrders(
            @Parameter(description = "индекс страницы, по умолчанию = 0")
            @RequestParam(required = false, defaultValue = "0")
            Integer pageInd,
            @Parameter(description = "размер страницы, по умолчанию = 10")
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize,
            @Parameter(hidden = true)
            UserDetailsImpl userDetails);
}

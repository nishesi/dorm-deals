package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

import java.security.Principal;

@Tags(value = {
        @Tag(name = "Users")
})
@RequestMapping("/user")
@Schema(description = "Работа с пользователем")
public interface UserApi {

    @Operation(summary = "регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "message", value = "некоторое сообщение"))
            ),
            @ApiResponse(responseCode = "400", description = "пользователь существует",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class)))
    })
    @PostMapping
    ResponseEntity<?> addUser(@RequestBody @Valid NewUserDto userDto);

    @Operation(summary = "Получение информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
                    })})
    @GetMapping
    UserDto getUser(Principal principal);

    @Operation(summary = "обновление информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "user",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorsDto.class)))
    })
    @PutMapping
    UserDto updateUser(Principal principal, @RequestBody @Valid UpdateUserDto userDto);

    @Operation(summary = "удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user deleted"),
            @ApiResponse(responseCode = "404", description = "user not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))
                    })})
    @DeleteMapping
    ResponseEntity<?> deleteUser(Principal principal);
}

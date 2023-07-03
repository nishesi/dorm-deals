package ru.itis.master.party.dormdeals.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.dto.review.NewReviewDto;
import ru.itis.master.party.dormdeals.dto.review.ReviewDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;


@Tags(value = {
        @Tag(name = "Reviews")
})
@RequestMapping("/products")
public interface ReviewApi {
    @Operation(summary = "Добавление нового отзыв")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Добавленный отзыв",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping("/{product-id}/review")
    ResponseEntity<ReviewDto> addReviewOnProduct(@RequestBody @Valid NewReviewDto newReviewDto,
                                                 @PathVariable("product-id") Long productId,
                                                 UserDetailsImpl userDetails);

    @Operation(summary = "Редактирование отзыва")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Отредактированный отзыв",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class))
                    }
            ),
            @ApiResponse(responseCode = "422", description = "невалидные данные",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorsDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PutMapping("/{product-id}/review")
    ResponseEntity<ReviewDto> updateReviewOnProduct(@RequestBody @Valid NewReviewDto newReviewDto,
                                                    @PathVariable("product-id") Long productId,
                                                    UserDetailsImpl userDetails);
    @Operation(summary = "Удаление отзыва")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Отзыв удален"),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{product-id}/review")
    ResponseEntity<?> deleteReviewOnProduct(@PathVariable("product-id") Long productId,
                                            UserDetailsImpl userDetails);
}

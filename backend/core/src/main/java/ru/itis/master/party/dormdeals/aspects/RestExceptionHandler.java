package ru.itis.master.party.dormdeals.aspects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler {
    private static ResponseEntity<ExceptionDto> formResponse(HttpStatus status, Exception ex) {
        return ResponseEntity
                .status(status)
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(status.value())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handle(NotFoundException ex) {
        return formResponse(NOT_FOUND, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handle(IllegalArgumentException ex) {
        return formResponse(BAD_REQUEST, ex);
    }

    @ExceptionHandler({
            NotAcceptableException.class,
            DataIntegrityViolationException.class,
            NotEnoughException.class
    })
    public ResponseEntity<ExceptionDto> handle(NotAcceptableException ex) {
        return formResponse(NOT_ACCEPTABLE, ex);
    }

    @Builder
    @ResponseStatus(NOT_FOUND)
    @Schema(description = "Сведения об ошибке")
    public record ExceptionDto(

            @Schema(description = "Текст ошибки", example = "Описание ошибки")
            String message,

            @Schema(description = "HTTP-код ошибки", example = "404")
            int statusCode
    ) {
    }
}

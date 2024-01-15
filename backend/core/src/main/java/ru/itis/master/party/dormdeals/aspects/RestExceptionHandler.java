package ru.itis.master.party.dormdeals.aspects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.exceptions.*;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handle(NotFoundException ex) {
        return formResponse(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handle(IllegalArgumentException ex) {
        return formResponse(BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler({
            NotAcceptableException.class,
            DataIntegrityViolationException.class,
            NotEnoughException.class
    })
    public ResponseEntity<ExceptionDto> handle(NotAcceptableException ex) {
        return formResponse(NOT_ACCEPTABLE, ex.getMessage());
    }

    private static ResponseEntity<ExceptionDto> formResponse(HttpStatus notAcceptable, String ex) {
        return ResponseEntity.status(notAcceptable).body(
                ExceptionDto.builder()
                        .message(ex)
                        .statusCode(notAcceptable.value())
                        .build()
        );
    }
}

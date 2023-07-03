package ru.itis.master.party.dormdeals.aspects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.master.party.dormdeals.dto.ExceptionDto;
import ru.itis.master.party.dormdeals.exceptions.*;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handle(NotFoundException ex) {
        return formResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handle(IllegalArgumentException ex) {
        return formResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<ExceptionDto> handle(NotAcceptableException ex) {
        return formResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> handle(DataIntegrityViolationException ex) {
        return formResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }

    @ExceptionHandler(NotEnoughException.class)
    public ResponseEntity<ExceptionDto> handle(NotEnoughException ex) {
        return formResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
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

package ru.itis.master.party.dormdeals.aspects;

import jakarta.persistence.ElementCollection;
import org.aspectj.weaver.ast.Not;
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handle(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ExceptionDto> handle(NotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .build()
        );
    }


    @ExceptionHandler(NotCreateSecondShop.class)
    public ResponseEntity<ExceptionDto> handle(NotCreateSecondShop ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> handle(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .build()
        );
    }

    @ExceptionHandler(MostAddedProductsInFavouriteException.class)
    public ResponseEntity<ExceptionDto> handle(MostAddedProductsInFavouriteException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .build()
        );
    }

    @ExceptionHandler(NotEnoughProductException.class)
    public ResponseEntity<ExceptionDto> handle(NotEnoughProductException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .build()
        );
    }
}

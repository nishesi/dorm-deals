package ru.itis.master.party.dormdeals.validation.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorDto;
import ru.itis.master.party.dormdeals.validation.responses.ValidationErrorsDto;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidationErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {

        List<ValidationErrorDto> errors = ex
                .resolveErrorMessages(messageSource, locale)
                .entrySet()
                .stream()
                .map(entry -> {
                    ObjectError objectError = entry.getKey();
                    var vedBuilder = ValidationErrorDto.builder();

                    if (objectError instanceof FieldError)
                        vedBuilder.field(((FieldError) objectError).getField());

                    vedBuilder
                            .object(objectError.getObjectName())
                            .message(entry.getValue());
                    return vedBuilder.build();
                })
                .toList();

//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String errorMessage = error.getDefaultMessage();
//
//            String fieldName = null;
//            String objectName = error.getObjectName();
//
//            if (error instanceof FieldError) {
//                fieldName = ((FieldError) error).getField();
//            }
//            ValidationErrorDto errorDto = ValidationErrorDto.builder()
//                    .message(errorMessage)
//                    .field(fieldName)
//                    .object(objectName)
//                    .build();
//
//            errors.add(errorDto);
//        });

        return new ValidationErrorsDto(errors);
    }

}

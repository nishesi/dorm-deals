package ru.itis.master.party.dormdeals.validation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorDto {
    @Schema(description = "название поля", example = "email", nullable = true)
    private String field;
    @Schema(description = "значение невалидного поля", example = "@email.ru", nullable = true)
    private Object value;
    @Schema(description = "название объекта", example = "newUserDto")
    private String object;
    @Schema(description = "сообщение ошибки", example = "Не валидный email адрес.")
    private String message;
}

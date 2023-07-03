package ru.itis.master.party.dormdeals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
@Schema(description = "Сведения об ошибке")
public class ExceptionDto {
    @Schema(description = "Текст ошибки", example = "Описание ошибки")
    private String message;
    @Schema(description = "HTTP-код ошибки", example = "404")
    private int statusCode;
}

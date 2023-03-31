package ru.itis.master.party.dormdeals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageDto {
    @Schema(description = "сообщение", example = "фух сервер не упал, твой запрос обработался")
    private String message;
}

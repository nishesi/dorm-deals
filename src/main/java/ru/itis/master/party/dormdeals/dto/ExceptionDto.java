package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDto {
    private String message;
    private int statusCode;
}

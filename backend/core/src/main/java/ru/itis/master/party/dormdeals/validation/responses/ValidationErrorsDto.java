package ru.itis.master.party.dormdeals.validation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationErrorsDto {
    private List<ValidationErrorDto> errors;
}

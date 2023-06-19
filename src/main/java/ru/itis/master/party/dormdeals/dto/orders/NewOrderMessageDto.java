package ru.itis.master.party.dormdeals.dto.orders;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderMessageDto {
    @NotBlank
    private String message;
}

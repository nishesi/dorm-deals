package ru.itis.master.party.dormdeals.dto.order;

import jakarta.validation.constraints.NotBlank;

public record NewOrderMessageForm(
        @NotBlank
        String message
) {
}

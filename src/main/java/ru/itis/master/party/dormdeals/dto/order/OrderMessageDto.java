package ru.itis.master.party.dormdeals.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessageDto {
    private Long userId;
    private String message;
    private ZonedDateTime addedDate;
}

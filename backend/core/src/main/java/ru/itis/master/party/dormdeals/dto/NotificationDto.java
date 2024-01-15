package ru.itis.master.party.dormdeals.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {
    private Long id;
    private String message;
    private ZonedDateTime dateTime;
}

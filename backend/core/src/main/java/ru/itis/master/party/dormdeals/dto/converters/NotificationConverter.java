package ru.itis.master.party.dormdeals.dto.converters;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.NotificationDto;
import ru.itis.master.party.dormdeals.models.Notification;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationConverter {
    public NotificationDto from(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .dateTime(notification.getCreatedDateTime())
                .build();
    }

    public List<NotificationDto> from(List<Notification> notifications) {
        return notifications.stream()
                .map(this::from)
                .collect(Collectors.toList());
    }
}

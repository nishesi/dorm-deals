package ru.itis.master.party.dormdeals.mapper;


import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import ru.itis.master.party.dormdeals.dto.NotificationDto;
import ru.itis.master.party.dormdeals.models.jpa.Notification;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        builder = @Builder(disableBuilder = true))
public interface NotificationMapper {

    NotificationDto toNotificationDto(Notification notification);

    List<NotificationDto> toNotificationDtoList(List<Notification> notifications);
}

package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.NotificationDto;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;

import java.util.List;

public interface NotificationService {
    void sendNotificationOrder(Order order, String message);
    void updateUnreadNotificationCountForUser(long userId);
    List<NotificationDto> getNotifications(long userId);

    void readNotification(Long notificationId, long userId);
}

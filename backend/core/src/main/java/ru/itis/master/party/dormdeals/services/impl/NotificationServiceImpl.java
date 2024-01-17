package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.NotificationDto;
import ru.itis.master.party.dormdeals.mapper.NotificationMapper;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.jpa.Notification;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;
import ru.itis.master.party.dormdeals.repositories.jpa.NotificationRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.UserRepository;
import ru.itis.master.party.dormdeals.services.NotificationService;

import java.time.ZonedDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    @Override
    public void sendNotificationOrder(Order order, String message) {
        Notification notification = Notification.builder()
                .notificationType(Notification.Type.ORDER_NOTIFICATION)
                .sender(order.getShop())
                .receiver(order.getCustomer())
                .receiverRead(false)
                .message(message)
                .createdAt(ZonedDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void readNotification(Long notificationId, long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(Notification.class, "id", notificationId));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new NotAcceptableException("Have not permission");
        }

        notification.setReceiverRead(true);
        notificationRepository.save(notification);

        updateUnreadNotificationCountForUser(userId);
    }

    @Override
    public void updateUnreadNotificationCountForUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(User.class, "id", userId));
        user.setCountUnreadNotifications(notificationRepository.getCountByReceiverId(userId));

        userRepository.save(user);
    }

    @Override
    public List<NotificationDto> getNotifications(long userId) {
        return notificationMapper.toNotificationDtoList(notificationRepository.findAllByReceiverId(userId));
    }
}

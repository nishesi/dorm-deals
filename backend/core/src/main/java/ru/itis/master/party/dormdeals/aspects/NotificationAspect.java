package ru.itis.master.party.dormdeals.aspects;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.repositories.OrderRepository;
import ru.itis.master.party.dormdeals.services.NotificationService;


@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    @Pointcut("execution(* ru.itis.master.party.dormdeals.services.impl.OrderServiceImpl.updateOrderState(long, Long, ..)) && args(userId, orderId, ..)")
    public void updateOrderStatePointcut(long userId, Long orderId) {}

    @After("updateOrderStatePointcut(userId, orderId)")
    public void sendNotificationAfterUpdate(long userId, Long orderId) {
        Order order = orderRepository.findWithShopById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId));

        notificationService.sendNotificationOrder(order, "Order " + orderId + " updated. " + "Status: " + order.getState());
        notificationService.updateUnreadNotificationCountForUser(order.getCustomer().getId());

    }
}
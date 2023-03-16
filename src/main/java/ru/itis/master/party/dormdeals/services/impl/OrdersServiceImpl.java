package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.OrderDto.NewOrder;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.repositories.OrdersRepository;
import ru.itis.master.party.dormdeals.services.OrdersService;

import static ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto.from;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;

    @Override
    public OrderDto getOrder(long id) {
        return from(getOrderOrThrow(id));
    }

    @Override
    public OrderDto createOrder(NewOrder newOrder) {
        Order order = Order.builder()
                .user(newOrder.getUser())
                .shop(newOrder.getShop())
                .orderTime(newOrder.getOrderTime())
                .userComment(newOrder.getUserComment())
                .state(Order.State.IN_PROCESSING)
                .build();

        ordersRepository.save(order);

        return from(order);
    }

    @Override
    public OrderDto updateOrderState(Long id, Order.State state) {
        Order order = getOrderOrThrow(id);
        order.setState(state);

        return from(ordersRepository.save(order));
    }


    @Override
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }

    private Order getOrderOrThrow(long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с идентификатором <" + id + "> не найден"));
    }
}

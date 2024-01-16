package ru.itis.master.party.dormdeals.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.OrderApi;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.dto.order.NewOrderDto;
import ru.itis.master.party.dormdeals.dto.order.NewOrderMessageDto;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.OrderService;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid NewOrderDto newOrderDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        long userId = userDetails.getUser().getId();
        orderService.createOrder(userId, newOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<OrderDto> updateOrderState(
            Long orderId,
            Order.State state,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.updateOrderState(userDetails.getUser().getId(), orderId, state);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<OrderDto> getOrder(Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @Override
    public ResponseEntity<?> addMessage(
            Long orderId,
            @Valid NewOrderMessageDto newOrderMessageDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.addOrderMessage(userDetails.getUser().getId(), orderId, newOrderMessageDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

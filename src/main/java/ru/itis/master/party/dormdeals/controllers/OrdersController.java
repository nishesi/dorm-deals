package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.OrdersApi;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderDto;
import ru.itis.master.party.dormdeals.dto.OrderDto.OrderWithProducts;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart;
import ru.itis.master.party.dormdeals.models.Order;
import ru.itis.master.party.dormdeals.services.OrdersService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrdersController implements OrdersApi {
    private final OrdersService ordersService;

    @Override
    public ResponseEntity<List<OrderDto>> createOrder(List<ProductDtoCart> productDtoCartList) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordersService.createOrder(productDtoCartList));
    }

    @Override
    public ResponseEntity<OrderDto> getOrder(Long orderId) {
        return ResponseEntity.ok(ordersService.getOrder(orderId));
    }

    @Override
    public ResponseEntity<OrderDto> updateOrderStateToConfirmed(Long orderId) {
        return ResponseEntity.accepted().body(ordersService.updateOrderState(orderId, Order.State.CONFIRMED));
    }

    @Override
    public ResponseEntity<OrderDto> updateOrderStateToInDelivery(Long orderId) {
        return ResponseEntity.accepted().body(ordersService.updateOrderState(orderId, Order.State.IN_DELIVERY));
    }

    @Override
    public ResponseEntity<OrderDto> updateOrderStateToDelivered(Long orderId) {
        return ResponseEntity.accepted().body(ordersService.updateOrderState(orderId, Order.State.DELIVERED));
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long orderId) {
        ordersService.deleteOrder(orderId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<OrderWithProducts> getAllProductsThisOrder(Long orderId) {
        return ResponseEntity.ok().body(ordersService.getAllProductsThisOrder(orderId));
    }
}

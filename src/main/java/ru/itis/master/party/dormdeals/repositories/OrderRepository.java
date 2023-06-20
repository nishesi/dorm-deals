package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "order-user-shop", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAllWithUserAndShopsByUserId(long userId, Pageable pageable);

    @EntityGraph(value = "order-user-shop", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAllWithUserAndShopsByShopId(long shopId, Pageable pageable);

    @EntityGraph(value = "order-product", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findOrderWithProductsById(Long orderId);
}

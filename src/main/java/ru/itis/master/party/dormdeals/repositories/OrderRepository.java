package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "order-customer-shop", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAllWithCustomerAndShopByCustomerId(long userId, Pageable pageable);

    @EntityGraph(value = "order-customer-shop", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAllWithUserAndShopByShopId(long shopId, Pageable pageable);

    @EntityGraph(value = "order-customer-shop", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findWithCustomerAndShopById(long orderId);

    @EntityGraph(value = "order-shop", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findWithShopById(long orderId);
}

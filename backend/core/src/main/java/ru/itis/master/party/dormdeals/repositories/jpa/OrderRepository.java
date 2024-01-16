package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.master.party.dormdeals.models.jpa.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "order-shop", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAllWithShopByCustomerId(long userId, Pageable pageable);

    @Query(value = "select o from Order o join fetch o.customer",
            countQuery = "select count(o) from Order o")
    Page<Order> findAllWithCustomerByShopId(long shopId, Pageable pageable);

    @EntityGraph(value = "order-customer-shop", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findWithCustomerAndShopById(long orderId);

    @EntityGraph(value = "order-shop", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findWithShopById(long orderId);
}

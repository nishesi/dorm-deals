package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(long userId, Pageable pageable);
    Page<Order> findAllByShopId(long shopId, Pageable pageable);
}

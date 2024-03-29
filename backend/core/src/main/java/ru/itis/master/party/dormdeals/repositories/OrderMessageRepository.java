package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.order.OrderMessage;

public interface OrderMessageRepository extends JpaRepository<OrderMessage, Long> {
    Page<OrderMessage> findAllByOrderId(Long orderId, Pageable pageable);
}

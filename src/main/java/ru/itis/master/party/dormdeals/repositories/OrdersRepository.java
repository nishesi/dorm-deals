package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.order.Order;

public interface OrdersRepository extends CrudRepository<Order, Long> {
}

package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.OrderProduct;

import java.util.List;

public interface OrderProductsRepository extends CrudRepository<OrderProduct, Long> {
    List<OrderProduct> findAllByOrderId(Long orderId);
}

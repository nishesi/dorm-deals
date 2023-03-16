package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.OrderProduct;

public interface OrderProductsRepository extends CrudRepository<OrderProduct, Long> {
}

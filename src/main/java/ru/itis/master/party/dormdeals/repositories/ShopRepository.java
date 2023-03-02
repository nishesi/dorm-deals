package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Shop;

public interface ShopRepository extends CrudRepository<Shop, Long> {
}

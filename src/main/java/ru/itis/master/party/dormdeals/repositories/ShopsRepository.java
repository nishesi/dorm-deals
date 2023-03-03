package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Shop;

public interface ShopsRepository extends CrudRepository<Shop, Long> {
    Page<Shop> findAllByOrderByIdAsc(Pageable pageable);
}

package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Shop;

import java.util.Optional;

public interface ShopsRepository extends CrudRepository<Shop, Long> {
    Page<Shop> findAllByOrderByIdAsc(Pageable pageable);
    Optional<Shop> findShopByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
    boolean existsByName(String name);

    @Query("select s.resource from Shop s where s.id=:id")
    String getResourceById(Long id);
}

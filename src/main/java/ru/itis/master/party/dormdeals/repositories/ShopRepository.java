package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.master.party.dormdeals.models.Shop;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Page<Shop> findAllByOrderByIdAsc(Pageable pageable);
    Optional<Shop> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
    boolean existsByName(String name);

    @Query("select s.resource from Shop s where s.id=:id")
    String getResourceById(Long id);
}

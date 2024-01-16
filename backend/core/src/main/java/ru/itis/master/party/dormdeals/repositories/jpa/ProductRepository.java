package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.master.party.dormdeals.models.jpa.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByShopIdAndStateOrderById(Long shopId, Product.State state, Pageable pageable);

    @Query("select p from Product p join fetch p.shop where p.id = :productId")
    Optional<Product> findWithShopById(Long productId);

    void deleteAllByShopId(Long shopId);

    @Query("select p from Product p left join fetch p.resources where p.id in :ids")
    List<Product> findAllWithResourcesByIdIn(List<Long> ids);

    @Query("select p.resources from Product p where p.id=:id")
    List<String> getResourceById(Long id);
}

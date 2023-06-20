package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStateOrderById(Product.State state, Pageable pageable);

    Page<Product> findAllByShopIdAndStateOrderById(Long shopId, Product.State state, Pageable pageable);

    void deleteAllByShopId(Long shopId);

    @Query("select p.resources from Product p where p.id=:id")
    List<String> getResourceById(Long id);

    @EntityGraph(value = "product-shop", type = EntityGraph.EntityGraphType.FETCH)
    List<Product> findAllProductWithShopByIdIn(Collection<Long> ids);
}

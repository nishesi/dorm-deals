package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.Product;

import java.awt.print.Pageable;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStateOrderById(Product.State state, org.springframework.data.domain.Pageable pageable);
}

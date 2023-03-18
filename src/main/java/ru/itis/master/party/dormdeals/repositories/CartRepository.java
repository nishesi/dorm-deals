package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserIdAndProductId(Long userId, Long productId);
    List<Cart> findByUserId(Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
}

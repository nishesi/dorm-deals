package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}

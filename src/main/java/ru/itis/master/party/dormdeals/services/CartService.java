package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.CartDto;

public interface CartService {
    void addCart(Long productId);

    CartDto getCart();

    void deleteCart(Long productId);
}

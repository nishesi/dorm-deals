package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.CartDto;

public interface CartService {
    void addCart(Long productId);

    CartDto getCart(String cookieHeader);

    void deleteCart(Long productId);

    void setCountProduct(Long productId, Integer count);
}

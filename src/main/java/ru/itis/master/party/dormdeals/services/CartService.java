package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.CartDto;

public interface CartService {
    void addCart(long userId, Long productId);

    CartDto getCart(String cookieHeader);

    CartDto getCart(long userId, String cookieHeader);

    void deleteCart(long userId, Long productId);

    void setCountProduct(long userId, Long productId, Integer count);
}

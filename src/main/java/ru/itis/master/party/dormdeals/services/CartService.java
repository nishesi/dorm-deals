package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.CartDto;

import java.util.List;

public interface CartService {
    void addCart(long userId, Long productId);

    CartDto getCart(List<Long> productsId);

    CartDto getCart(long userId, List<Long> productsId);

    void cartSynchronization(long userId, List<CartCookie> cartsCookie);
}

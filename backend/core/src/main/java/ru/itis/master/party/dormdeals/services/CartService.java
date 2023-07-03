package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;

import java.util.List;

public interface CartService {
    List<CartProductDto> getCart(long userId);

    void cartSynchronization(long userId, List<CartCookie> cartsCookie);
}

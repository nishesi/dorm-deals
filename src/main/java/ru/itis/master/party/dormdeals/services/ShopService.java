package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.shop.NewShop;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.ShopsPage;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;

public interface ShopService {

    ShopDto getShop(Long id);

    ShopsPage getAllShops(int page);

    ShopDto createShop(long userId, NewShop newShop);

    ShopDto updateShop(long userId, Long id, UpdateShop updateShop);

    void deleteShop(long userId);

    ShopWithProducts getAllProductsThisShop(Long shopId, int page);
}

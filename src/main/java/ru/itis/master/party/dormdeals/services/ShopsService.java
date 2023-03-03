package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ShopDto;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.dto.ShopsPage;

public interface ShopsService {
    ShopDto getShop(long id);

    ShopsPage getAllShops(int page);

    ShopDto addShop(ShopDto shopDto);

    ShopDto updateShop(Long id, ShopDto updatedShopDto);

    void deleteShop(Long id);

    Shop getShopOrThrow(long id);
}

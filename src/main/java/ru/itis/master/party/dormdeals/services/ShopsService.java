package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.models.ShopWithProducts;

public interface ShopsService {
    ShopDto getShop(long id);

    ShopsPage getAllShops(int page);

    ShopDto createShop(NewShop newShop, Long ownerId);

    ShopDto updateShop(Long id, UpdateShop updateShop);

    void deleteShop(Long id);

    Shop getShopOrThrow(long id);

    ShopWithProducts getAllProductsThisShop(Long shopId, int page);
}

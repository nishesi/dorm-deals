package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ShopDto.NewShop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.dto.ShopDto.UpdateShop;
import ru.itis.master.party.dormdeals.dto.ShopWithProducts;

public interface ShopsService {

    ShopDto getShop(Long id);

    ShopsPage getAllShops(int page);

    ShopDto createShop(String ownerEmail, NewShop newShop);

    ShopDto updateShop(Long id, UpdateShop updateShop);

    void deleteShop(Long id);

    ShopWithProducts getAllProductsThisShop(Long shopId, int page);
}

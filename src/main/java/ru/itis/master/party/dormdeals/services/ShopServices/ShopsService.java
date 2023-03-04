package ru.itis.master.party.dormdeals.services.ShopServices;

import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;

public interface ShopsService {
    ShopDto getShop(long id);

    ShopsPage getAllShops(int page);

    ShopDto createShop(ShopDto shopDto, Long ownerId);

    ShopDto updateShop(Long id, ShopDto updatedShopDto);

    void deleteShop(Long id);

    Shop getShopOrThrow(long id);
}

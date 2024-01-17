package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.shop.NewShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopWithProductsDto;

public interface ShopService {

    ShopDto createShop(long userId, NewShopForm newShopForm);

    ShopDto updateShop(long userId, Long id, UpdateShopForm updateShopForm);

    void deleteShop(long userId);

    ShopWithProductsDto getAllProductsThisShop(Long shopId, int page);

    void updateShopImage(long userId, MultipartFile shopImage);
}

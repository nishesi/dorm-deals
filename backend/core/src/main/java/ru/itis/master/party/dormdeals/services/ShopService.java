package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.shop.NewShopForm;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.shop.UpdateShopForm;

public interface ShopService {

    ShopDto createShop(long userId, NewShopForm form);

    ShopDto getShop(Long shopId);

    ShopDto updateShop(long userId, Long id, UpdateShopForm form);

    void deleteShop(long userId);

    void updateShopImage(long userId, MultipartFile shopImage);
}

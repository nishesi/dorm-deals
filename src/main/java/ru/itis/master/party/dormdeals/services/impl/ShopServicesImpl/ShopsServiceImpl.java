package ru.itis.master.party.dormdeals.services.impl.ShopServicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopsPage;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.services.ShopServices.ShopsService;

import static ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto.from;

@Service
@RequiredArgsConstructor
public class ShopsServiceImpl implements ShopsService {
    private final ShopsRepository shopsRepository;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ShopDto getShop(long id) {
        return from(getShopOrThrow(id));
    }

    @Override
    public ShopsPage getAllShops(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Shop> shopsPage = shopsRepository.findAllByOrderByIdAsc(pageRequest);

        return ShopsPage.builder()
                .shops(from(shopsPage.getContent()))
                .totalPagesCount(shopsPage.getTotalPages())
                .build();
    }

    @Override
    public ShopDto addShop(ShopDto shopDto) {
        Shop shop = Shop.builder()
                .name(shopDto.getName())
                .description(shopDto.getDescription())
                .rating(shopDto.getRating())
//                .owner(shopDto.getOwner())
                .build();

        shopsRepository.save(shop);

        return from(shop);
    }

    @Override
    public ShopDto updateShop(Long id, ShopDto updatedShopDto) {
        Shop shopForUpdate = getShopOrThrow(id);

        shopForUpdate.setName(updatedShopDto.getName());
        shopForUpdate.setDescription(updatedShopDto.getDescription());
        shopForUpdate.setRating(updatedShopDto.getRating());
//        shopForUpdate.setOwner(updatedShopDto.getOwner());

        shopsRepository.save(shopForUpdate);

        return from(shopForUpdate);
    }

    @Override
    public void deleteShop(Long id) {
        shopsRepository.deleteById(id);
    }

    @Override
    public Shop getShopOrThrow(long id) {
        return shopsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Магазин с идентификатором <" + id + "> не найден"));
    }
}

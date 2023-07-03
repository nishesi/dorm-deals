package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.dto.user.UserDtoForShopAndReview;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShopConverter {
    private final ResourceUrlResolver resolver;
    private final DormitoryConverter dormitoryConverter;

    public ShopDto from(Shop shop) {
        String imageUrl = resolver.resolveUrl(FileType.IMAGE, EntityType.SHOP, String.valueOf(shop.getId()));

        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .rating(shop.getRating())
                .dormitories(dormitoryConverter.from(shop.getDormitories()))
                .owner(UserDtoForShopAndReview.from(shop.getOwner()))
                .resourceUrl(imageUrl)
                .build();
    }

    public List<ShopDto> from(List<Shop> shops) {
        return shops
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

}

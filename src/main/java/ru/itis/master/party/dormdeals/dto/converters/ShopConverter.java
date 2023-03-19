package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ShopDto.ShopDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDtoForShop;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShopConverter {
    private final ResourceUrlResolver resolver;
    private final DormitoryConverter dormitoryConverter;

    public ShopDto from(Shop shop) {
        String imageUrl = resolver.resolveUrl(shop.getId().toString(), ResourceType.SHOP_IMAGE);

        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .rating(shop.getRating())
                .dormitories(dormitoryConverter.from(shop.getDormitories()))
                .owner(UserDtoForShop.from(shop.getOwner()))
                .shopImageUrl(imageUrl)
                .build();
    }

    public List<ShopDto> from(List<Shop> shops) {
        return shops
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

}

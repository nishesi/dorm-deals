package ru.itis.master.party.dormdeals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopDto {
    private String name;
    private String description;
    private int rating;
    private User owner;

    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .rating(shop.getRating())
                .owner(shop.getOwner())
                .build();
    }
}

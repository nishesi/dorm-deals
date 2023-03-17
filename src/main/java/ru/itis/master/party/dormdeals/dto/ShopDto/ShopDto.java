package ru.itis.master.party.dormdeals.dto.ShopDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDtoForShop;
import ru.itis.master.party.dormdeals.models.Shop;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Магазин")
public class ShopDto {
    @Schema(description = "название магазина", example = "TopShop")
    private String name;
    @Schema(description = "описание магазина", example = "Here we sell the best products")
    private String description;
    @Schema(description = "рейтинг магазина", example = "4.9")
    private double rating;
    @Schema(description = "место продаж", example = "Пушкина, 9")
    private String placeSells;
    @Schema(description = "владелец магазина")
    private UserDtoForShop owner;

    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .rating(shop.getRating())
                .placeSells(shop.getPlaceSells())
                .owner(UserDtoForShop.from(shop.getOwner()))
                .build();
    }

    public static List<ShopDto> from(List<Shop> shops) {
        return shops
                .stream()
                .map(ShopDto::from)
                .collect(Collectors.toList());
    }

}

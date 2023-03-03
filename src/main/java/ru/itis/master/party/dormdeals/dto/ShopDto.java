package ru.itis.master.party.dormdeals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Shop;
//import ru.itis.master.party.dormdeals.models.User;

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
    @Schema(description = "рейтинг магазина", example = "5")
    private int rating;
//    @Schema(description = "владелец магазина")
//    private User owner;

    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .rating(shop.getRating())
//                .owner(shop.getOwner())
                .build();
    }

    public static List<ShopDto> from(List<Shop> shops) {
        return shops
                .stream()
                .map(ShopDto::from)
                .collect(Collectors.toList());
    }

}

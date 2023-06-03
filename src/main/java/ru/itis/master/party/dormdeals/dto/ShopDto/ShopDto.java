package ru.itis.master.party.dormdeals.dto.ShopDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.dto.DormitoryDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDtoForShop;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Магазин")
public class ShopDto {

    @Schema(description = "идентификатор магазина", example = "100500")
    private Long id;

    @Schema(description = "название магазина", example = "TopShop")
    private String name;

    @Schema(description = "описание магазина", example = "Here we sell the best products")
    private String description;

    @Schema(description = "рейтинг магазина", example = "4.9")
    private double rating;

    @Schema(description = "место продаж", example = "Пушкина, 9")
    private List<DormitoryDto> dormitories;

    @Schema(description = "владелец магазина")
    private UserDtoForShop owner;

    @Schema(description = "изображение магазина")
    private String resourceUrl;
}

package ru.itis.master.party.dormdeals.dto.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Магазин")
public class ShopDto {

    @Schema(description = "идентификатор магазина", example = "100500")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @Schema(description = "название магазина", example = "TopShop")
    private String name;

    @Schema(description = "описание магазина", example = "Here we sell the best products")
    private String description;

    @Schema(description = "рейтинг магазина", example = "4.9")
    private float rating;

    @Schema(description = "место продаж", example = "Пушкина, 9")
    private List<Dormitory> dormitories;

    @Schema(description = "владелец магазина")
    private Owner owner;

    @Schema(description = "изображение магазина")
    private String resourceUrl;

    public record Owner(
            Long id,
            String firstName,
            String lastName) {
    }

    public record Dormitory(
            Long id,
            String name,
            String address
    ) {
    }
}

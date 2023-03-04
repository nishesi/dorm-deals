package ru.itis.master.party.dormdeals.dto.ShopDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Обновленный магазин")
public class UpdateShop {
    private String name;
    private String description;
    private int rating;
    private String placeSells;
    private User owner;
}

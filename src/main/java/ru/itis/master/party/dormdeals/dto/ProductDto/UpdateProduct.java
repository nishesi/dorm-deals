package ru.itis.master.party.dormdeals.dto.ProductDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Обновление товара")
public class UpdateProduct {
    private String name;
    private String description;
    private Integer price;
    private Integer count_in_storage;
    //    private UUID uuid_photos;
}

package ru.itis.master.party.dormdeals.dto.ProductDto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Новый товар")
public class NewProduct {
    @NotBlank(message = "Поле обязательно к заполнению")
    @Size(min=2, max = 50, message = "Слишком длинное или короткое название")
    private String name;
    @NotBlank(message = "Поле обязательно к заполнению")
    @Size(min=10, max = 1000, message = "Слишком длинное или короткое описание")
    private String description;
    private String category;
    @NotBlank(message = "Поле обязательно к заполнению")
    @Size(min=1, max = 300000, message = "Слишком длинное или короткое название")
    private Integer price;

    private Integer countInStorage;
    private UUID uuidOfPhotos;
}

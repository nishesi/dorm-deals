package ru.itis.master.party.dormdeals.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Товар")
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer price;
    private Integer count_in_storage;
//    private UUID uuid_photos;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .count_in_storage(product.getCount_in_storage())
                .build();
    }

    public static List<ProductDto> from(List<Product> products) {
        return products
                .stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}

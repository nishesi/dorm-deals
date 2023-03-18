package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Товар")
public class ProductDto {

    @Schema(description = "идентификатор товара", example = "1")
    private Long id;

    @Schema(description = "название товара", example = "Adrenaline Rush")
    private String name;

    @Schema(description = "описание товара", example = "бодрит")
    private String description;
    @Schema(description = "категория товара", example = "продукты/напитки")
    private String category;
    @Schema(description = "цена товара", example = "100")
    private float price;
    @Schema(description = "количество на складе", example = "13")
    private short countInStorage;
    private List<String> imageUrlList;

    public static ProductDto from(Product product, ResourceUrlResolver resolver) {
        List<String> imageUrls = product.getImages().stream()
                .map(image -> resolver.resolveUrl(
                        image.getId(),
                        ResourceType.PRODUCT_IMAGE))
                .toList();
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .countInStorage(product.getCountInStorage())
                .imageUrlList(imageUrls)
                .build();
    }

    public static List<ProductDto> from(List<Product> products, ResourceUrlResolver resolver) {
        return products
                .stream()
                .map(product -> ProductDto.from(product, resolver))
                .collect(Collectors.toList());
    }
}

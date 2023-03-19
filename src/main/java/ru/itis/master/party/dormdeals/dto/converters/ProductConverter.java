package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ResourceUrlResolver resolver;
    public ProductDto from(Product product) {
        List<String> imageUrls = product.getImages().stream()
                .map(image -> resolver.resolveUrl(image.getId(), ResourceType.PRODUCT_IMAGE))
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

    public List<ProductDto> from(List<Product> products) {
        return products
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }
}

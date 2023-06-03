package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ResourceUrlResolver resolver;
    public ProductDto from(Product product) {
        List<String> imageUrls = IntStream.range(0, product.getResources().size())
                .mapToObj(index -> resolver.resolveUrl(product.getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, index + 1))
                .toList();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .countInStorage(product.getCountInStorage())
                .resources(imageUrls)
                .build();
    }

    public List<ProductDto> from(List<Product> products) {
        return products
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }
}

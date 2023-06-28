package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDtoForShop;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ResourceUrlResolver resolver;
    public ProductDto convertProductInProductDto(Product product) {

        //TODO: определять тип файла
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
                .state(product.getState())
                .resources(imageUrls)
                .shop(convertShopInShopDto(product.getShop()))
                .build();
    }

    public ProductDtoForShop convertProductInProductDtoForShop(Product product) {

        //TODO: определять тип файла
        List<String> imageUrls = IntStream.range(0, product.getResources().size())
                .mapToObj(index -> resolver.resolveUrl(product.getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, index + 1))
                .toList();

        return ProductDtoForShop.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .countInStorage(product.getCountInStorage())
                .state(product.getState())
                .resources(imageUrls)
                .build();
    }

    private ShopDto convertShopInShopDto(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .rating(shop.getRating())
                .resourceUrl(shop.getResource())
                .build();
    }

    public List<ProductDto> convertListProductInListProductDto(List<Product> products) {
        return products
                .stream()
                .map(this::convertProductInProductDto)
                .toList();
    }

    public List<ProductDtoForShop> convertListProductInListProductDtoForShop(List<Product> products) {
        return products
                .stream()
                .map(this::convertProductInProductDtoForShop)
                .toList();
    }
}

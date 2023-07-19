package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDtoForShop;
import ru.itis.master.party.dormdeals.dto.shop.ShopDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ResourceUrlResolver resolver;
    public ProductDto convertProductInProductDto(Product product) {

        //TODO: определять тип файла
        List<String> imageUrls = product.getResources().stream()
                .map(id -> resolver.resolveUrl(FileType.IMAGE, EntityType.PRODUCT, id))
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
        List<String> imageUrls = product.getResources().stream()
                .map(id -> resolver.resolveUrl(FileType.IMAGE, EntityType.PRODUCT, id))
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
        String imageUrl = resolver.resolveUrl(FileType.IMAGE, EntityType.SHOP, String.valueOf(shop.getId()));
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .rating(shop.getRating())
                .resourceUrl(imageUrl)
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

    public Page<ProductDto> from(Page<Product> products) {
        return products.map(product -> ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .countInStorage(product.getCountInStorage())
                .price(product.getPrice())
                .resources(new ArrayList<>(product.getResources()))
                .build());
    }
}

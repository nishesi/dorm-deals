package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.models.CartProduct;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartProductConverter {
    private final ResourceUrlResolver resolver;
    public CartProductDto fromCartProduct(CartProduct cartProduct) {
        List<String> images = cartProduct.getProduct().getResources();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(cartProduct.getProduct().getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, 1);
        return CartProductDto.builder()
                .productDto(
                        ProductDto.builder()
                                .id(cartProduct.getProduct().getId())
                                .name(cartProduct.getProduct().getName())
                                .price(cartProduct.getProduct().getPrice())
                                .countInStorage(cartProduct.getProduct().getCountInStorage())
                                .build()
                )
                .count(cartProduct.getCount())
                .state(cartProduct.getState())
                .coverImage(url)
                .build();
    }

    public CartProductDto fromProduct(Product product) {
        List<String> images = product.getResources();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(product.getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, 1);
        return CartProductDto.builder()
                .productDto(
                        ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .countInStorage(product.getCountInStorage())
                                .build()
                )
                .coverImage(url)
                .build();
    }

    public List<CartProductDto> listFromCartProduct(List<CartProduct> cartProducts) {
        return cartProducts.stream()
                .map(this::fromCartProduct)
                .collect(Collectors.toList());
    }

    public List<CartProductDto> listFromProduct(List<Product> products) {
        return products.stream()
                .map(this::fromProduct)
                .collect(Collectors.toList());
    }
}

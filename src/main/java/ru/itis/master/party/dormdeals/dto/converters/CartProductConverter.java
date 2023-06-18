package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartProductConverter {
    private final ResourceUrlResolver resolver;
    public CartProductDto fromCart(Cart cart) {
        List<String> images = cart.getProduct().getResources();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(cart.getProduct().getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, 1);
        return CartProductDto.builder()
                .id(cart.getProduct().getId())
                .name(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .count(cart.getCount())
                .state(cart.getState())
                .countInStorage(cart.getProduct().getCountInStorage())
                .coverImageUrl(url)
                .build();
    }

    public CartProductDto fromProduct(Product product) {
        List<String> images = product.getResources();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(product.getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, 1);
        return CartProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .countInStorage(product.getCountInStorage())
                .coverImageUrl(url)
                .build();
    }

    public <T> List<CartProductDto> from(List<T> objects, Class<T> clazz) {
        if (clazz == Product.class) {
            return objects.stream().map(obj -> this.fromProduct((Product) obj)).collect(Collectors.toList());
        } else if (clazz == Cart.class) {
            return objects.stream().map(obj -> this.fromCart((Cart) obj)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.ProductImage;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartProductConverter {
    private final ResourceUrlResolver resolver;
    public CartProductDto from(Cart cart) {
        List<ProductImage> images = cart.getProduct().getImages();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(images.get(0).getId(), ResourceType.PRODUCT_IMAGE);
        return CartProductDto.builder()
                .id(cart.getProduct().getId())
                .name(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .count(cart.getCount())
                .countInStorage(cart.getProduct().getCountInStorage())
                .coverImageUrl(url)
                .build();
    }

    public List<CartProductDto> from(List<Cart> carts) {
        return carts
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }
}

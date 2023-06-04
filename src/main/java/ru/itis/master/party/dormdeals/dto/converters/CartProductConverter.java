package ru.itis.master.party.dormdeals.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartProductConverter {
    private final ResourceUrlResolver resolver;
    public CartProductDto from(Cart cart) {
        List<String> images = cart.getProduct().getResources();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(cart.getProduct().getId(), File.FileDtoType.PRODUCT, File.FileType.IMAGE, 1);
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

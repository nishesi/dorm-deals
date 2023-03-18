package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.ProductImage;
import ru.itis.master.party.dormdeals.utils.ResourceType;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Товар")
public class ProductDtoCart {

    @Schema(description = "идентификатор товара", example = "1")
    private Long id;

    @Schema(description = "название товара", example = "Adrenaline Rush")
    private String name;
    @Schema(description = "цена товара", example = "100")
    private float price;
    @Schema(description = "количество в корзине", example = "100")
    private Integer countInCart;
    @Schema(description = "количество на складе", example = "13")
    private short countInStorage;
    private String coverImageUrl;


    public static ProductDtoCart from(Cart cart, ResourceUrlResolver resolver) {
        List<ProductImage> images = cart.getProduct().getImages();
        String url = (images.isEmpty()) ?
                "" :
                resolver.resolveUrl(images.get(0).getId(), ResourceType.PRODUCT_IMAGE);
        return ProductDtoCart.builder()
                .id(cart.getProduct().getId())
                .name(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .countInCart(cart.getCount())
                .countInStorage(cart.getProduct().getCountInStorage())
                .coverImageUrl(url)
                .build();
    }

    public static List<ProductDtoCart> from(List<Cart> carts, ResourceUrlResolver resolver) {
        return carts
                .stream()
                .map(cart -> from(cart, resolver))
                .collect(Collectors.toList());
    }
}
package ru.itis.master.party.dormdeals.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;

import java.util.List;
import java.util.UUID;
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
    private UUID uuidOfPhotos;


    public static ProductDtoCart from(Cart cart) {
        return ProductDtoCart.builder()
                .id(cart.getProduct().getId())
                .name(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .countInCart(cart.getCount())
                .countInStorage(cart.getProduct().getCountInStorage())
                .uuidOfPhotos(cart.getProduct().getUuidOfPhotos())
                .build();
    }

    public static List<ProductDtoCart> from(List<Cart> carts) {
        return carts
                .stream()
                .map(ProductDtoCart::from)
                .collect(Collectors.toList());
    }
}
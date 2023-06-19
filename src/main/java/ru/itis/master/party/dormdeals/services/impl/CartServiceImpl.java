package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.CartProduct;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.CartService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartProductConverter cartProductConverter;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ProductsRepository productsRepository;

    @Override
    public List<CartProductDto> getCart(long userId) {
        return cartProductConverter.listFromCartProduct(cartRepository.findByUserId(userId).getProductsInCart());
    }

    @Override
    public void cartSynchronization(long userId, List<CartCookie> cartsCookie) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cartsCookie.size() == 0) {
            cart.getProductsInCart().clear();
            cartRepository.save(cart);
            return;
        }

        cartsCookie.forEach(
                cartCookie -> {
                    CartProduct cartProduct = findCartProductsByIdAndCookieId(cart, cartCookie.getId());
                    if (cartProduct != null) {
                        if (!cartProduct.getCount().equals(cartCookie.getCount())) {
                            if (cartProduct.getProduct().getCountInStorage() < cartCookie.getCount()) {
                                throw new NotEnoughException(Cart.class, cartProduct.getProduct().getId(), cartCookie.getCount(), cartProduct.getProduct().getCountInStorage());
                            }
                            cartProduct.setCount(cartCookie.getCount());
                        }
                        if (!cartProduct.getState().equals(getStateFromCookie(cartCookie))) {
                            cartProduct.setState(getStateFromCookie(cartCookie));
                        }
                    } else {
//                        cart.getProductsInCart().add(createProductInCart(cartCookie));
                        cart.addProductToCart(createProductInCart(cartCookie));
                    }
                });
        cartRepository.save(cart);
    }


    private CartProduct.State getStateFromCookie(CartCookie cartCookie) {
        if (cartCookie.getStateProduct()) {
            return CartProduct.State.ACTIVE;
        }
        return CartProduct.State.INACTIVE;
    }

    private CartProduct findCartProductsByIdAndCookieId(Cart cart, Long productId) {
        return cart.getProductsInCart().stream()
                .filter(productCart -> productCart.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private CartProduct createProductInCart(CartCookie cartCookies) {
        return CartProduct.builder()
                .product(productsRepository.getReferenceById(cartCookies.getId()))
                .count(cartCookies.getCount())
                .state(getStateFromCookie(cartCookies))
                .build();
    }


//    private Double getSumOfProducts(List<CartProductDto> cartProductDto, List<Cart> cart) {
//        return cart.stream()
//                .mapToDouble(c -> cartProductDto.stream()
//                        .filter(cp -> cp.getName().equals(c.getProduct().getName()))
//                        .findFirst()
//                        .map(CartProductDto::getPrice)
//                        .orElse(0.0F) * c.getCount())
//                .reduce(0.0, Double::sum);
//    }

}

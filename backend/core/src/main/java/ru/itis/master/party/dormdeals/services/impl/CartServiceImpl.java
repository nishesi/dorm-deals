package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.product.CartProductDto;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.mapper.ProductMapper;
import ru.itis.master.party.dormdeals.models.jpa.Cart;
import ru.itis.master.party.dormdeals.models.jpa.CartProduct;
import ru.itis.master.party.dormdeals.repositories.jpa.CartRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.ProductRepository;
import ru.itis.master.party.dormdeals.services.CartService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ProductMapper productMapper;
    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    @Override
    public List<CartProductDto> getCart(long userId) {
        return productMapper.toCartProductDtoListFroCartProductList(cartRepository.findByUserId(userId).getProductsInCart());
    }

    @Override
    public void cartSynchronization(long userId, List<CartCookie> cartsCookie) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cartsCookie.isEmpty()) {
            cart.getProductsInCart().clear();
            cartRepository.save(cart);
            return;
        }

        Map<Long, CartProduct> cartProductMap = createCartProductMap(cart.getProductsInCart());

        cart.getProductsInCart().removeIf(cartProduct ->
                cartsCookie.stream().noneMatch(cartCookie ->
                        cartCookie.getId().equals(cartProduct.getProduct().getId())));

        for (CartCookie cartCookie : cartsCookie) {
            CartProduct cartProduct = cartProductMap.get(cartCookie.getId());
            if (cartProduct != null) {
                updateCartProduct(cartProduct, cartCookie);
            } else {
                cart.addProductToCart(createProductInCart(cartCookie));
            }
        }

        cartRepository.save(cart);
    }

    private void updateCartProduct(CartProduct cartProduct, CartCookie cartCookie) {
        if (!cartProduct.getCount().equals(cartCookie.getCount())) {
            int countInStorage = cartProduct.getProduct().getCountInStorage();
            if (countInStorage < cartCookie.getCount()) {
                throw new NotEnoughException(Cart.class, cartProduct.getProduct().getId(), cartCookie.getCount(), countInStorage);
            }
            cartProduct.setCount(cartCookie.getCount());
        }
        CartProduct.State cartCookieState = getStateFromCookie(cartCookie);
        if (!cartProduct.getState().equals(cartCookieState)) {
            cartProduct.setState(cartCookieState);
        }
    }

    private CartProduct.State getStateFromCookie(CartCookie cartCookie) {
        if (cartCookie.getStateProduct()) {
            return CartProduct.State.ACTIVE;
        }
        return CartProduct.State.INACTIVE;
    }

    private CartProduct createProductInCart(CartCookie cartCookie) {
        return CartProduct.builder()
                .product(productRepository.getReferenceById(cartCookie.getId()))
                .count(cartCookie.getCount())
                .state(getStateFromCookie(cartCookie))
                .build();
    }

    private Map<Long, CartProduct> createCartProductMap(List<CartProduct> cartProducts) {
        return cartProducts.stream()
                .collect(Collectors.toMap(cartProduct -> cartProduct.getProduct().getId(),
                        Function.identity()));
    }
}

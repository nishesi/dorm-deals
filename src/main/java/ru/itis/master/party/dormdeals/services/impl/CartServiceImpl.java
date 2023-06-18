package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartCookie;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.models.Cart;
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
        return cartProductConverter.from(cartRepository.findByUserId(userId), Cart.class);
    }

    @Override
    public void cartSynchronization(long userId, List<CartCookie> cartsCookie) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        cartsCookie.forEach(
                cartCookie -> {
                    Cart cart = findCartByIdAndCookieId(carts, cartCookie.getId());
                    if (cart != null) {
                        if (!cart.getCount().equals(cartCookie.getCount())) {
                            cart.setCount(cartCookie.getCount());
                        }
                        if (!cart.getState().equals(getStateFromCookie(cartCookie))) {
                            cart.setState(getStateFromCookie(cartCookie));
                        }
                    } else {
                        carts.add(createNewCart(userId, cartCookie));
                    }
                });
        cartRepository.saveAll(carts);
    }


    private Cart.State getStateFromCookie(CartCookie cartCookie) {
        if (cartCookie.getStateProduct()) {
            return Cart.State.ACTIVE;
        }
        return Cart.State.INACTIVE;
    }

    private Cart findCartByIdAndCookieId(List<Cart> carts, Long cartId) {
        return carts.stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    private Cart createNewCart(long userId, CartCookie cartCookies) {
        return Cart.builder()
                .user(userRepository.getReferenceById(userId))
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

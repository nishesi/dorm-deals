package ru.itis.master.party.dormdeals.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughProductException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.utils.GetOrThrow;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartProductConverter cartProductConverter;

    private final OwnerChecker ownerChecker;

    private final GetOrThrow getOrThrow;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ProductsRepository productsRepository;

    @Override
    public void addCart(Long productId) {
        User user = ownerChecker.initThisUser(userRepository);
        Product product = getOrThrow.getProductOrThrow(productId, productsRepository);
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId).orElse(null);

        if (product.getState().equals(Product.State.ACTIVE)) {
            if (cart != null && product.getCountInStorage() == cart.getCount()) {
                throw new NotEnoughProductException("На складе осталось только " + product.getCountInStorage() + " позиций");
            }

            if (cart != null && cart.getCount() >= 1) {
                cart.setCount(cart.getCount() + 1);
                cartRepository.save(cart);
            } else {
                cartRepository.save(Cart.builder()
                        .user(user)
                        .product(product)
                        .count(1)
                        .state(Cart.State.ACTIVE)
                        .build());
            }
        }
    }

    //TODO ЭТО ПРОСТО КРИНЖА надо всё переделать, но зато щас работает)))
    @Override
    @Transactional
    public CartDto getCart(String cookieHeader) {
        List<Long> productIdFromCookie = new ArrayList<>();
        List<Integer> productCountFromCookie = new ArrayList<>();
        Map<Long, Integer> productIdAndCountFromCookie = new HashMap<>();

        if (cookieHeader != null) {
            List<Cookie> cookies = Arrays.stream(cookieHeader.split(";"))
                    .map(String::trim)
                    .map(cookieString -> {
                        String[] parts = cookieString.split("=");
                        if (parts.length == 2) {
                            return new Cookie(parts[0], parts[1]);
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    productIdFromCookie = Arrays.stream(cookie.getValue().split("&")).map(Long::parseLong).collect(Collectors.toList());
                }
                if (cookie.getName().equals("count")) {
                    productCountFromCookie = Arrays.stream(cookie.getValue().split("&")).map(Integer::parseInt).collect(Collectors.toList());
                }
            }
            productIdAndCountFromCookie = IntStream.range(0, productIdFromCookie.size())
                    .boxed()
                    .collect(Collectors.toMap(productIdFromCookie::get, productCountFromCookie::get));
        }

        User user = ownerChecker.initThisUser(userRepository);

        if (user != null) {
            if (productIdAndCountFromCookie.size() > 0) {
                productIdAndCountFromCookie.forEach(((productId, count) -> {
                    addCart(productId);
                    setCountProduct(productId, count);
                }));
            }

            List<Cart> cart = cartRepository.findByUserId(user.getId());
            List<CartProductDto> cartProductDto = cartProductConverter.from(cart);


            return CartDto.builder()
                    .cartProductDto(cartProductDto)
                    .sumOfProducts(getSumOfProducts(cartProductDto, cart))
                    .build();
        } else {
            List<Integer> finalProductCountFromCookie = productCountFromCookie;
            List<Long> finalProductIdFromCookie = productIdFromCookie;
            List<Cart> cart = IntStream.range(0, productIdFromCookie.size())
                    .mapToObj(i -> Cart.builder()
                            .product(getOrThrow.getProductOrThrow(finalProductIdFromCookie.get(i), productsRepository))
                            .count(finalProductCountFromCookie.get(i))
                            .build()).collect(Collectors.toList());
            List<CartProductDto> cartProductDto = cartProductConverter.from(cart);

            return CartDto.builder()
                    .cartProductDto(cartProductDto)
                    .sumOfProducts(getSumOfProducts(cartProductDto, cart))
                    .build();
        }
    }

    @Transactional
    @Override
    public void deleteCart(Long productId) {
        User user = ownerChecker.initThisUser(userRepository);
        cartRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    @Override
    public void setCountProduct(Long productId, Integer count) {
        User user = ownerChecker.initThisUser(userRepository);
        Cart cart = getOrThrow.getCartOrThrow(user.getId(), productId, cartRepository);
        Product product = getOrThrow.getProductOrThrow(productId, productsRepository);

        if (count > product.getCountInStorage()) {
            throw new NotEnoughProductException("На складе осталось только " + product.getCountInStorage() + " позиций");
        } else if (count == 0) {
            deleteCart(productId);
        } else {
            cart.setCount(count);
            cartRepository.save(cart);
        }
    }

    //TODO сделать этот метод поумнее
    private Double getSumOfProducts(List<CartProductDto> cartProductDto, List<Cart> cart) {
        Map<String, Float> productsPrices = cartProductDto.stream()
                .collect(Collectors.toMap(CartProductDto::getName, CartProductDto::getPrice));

        return cart.stream().mapToDouble(i ->
                        (productsPrices.getOrDefault(i.getProduct().getName(), 0.0F) * i.getCount()))
                .sum();
    }

}

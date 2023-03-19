package ru.itis.master.party.dormdeals.services.impl;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart.from;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OwnerChecker ownerChecker;
    private final GetOrThrow getOrThrow;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;

    //TODO вот это мне ваще не нравится, тоже надо что то придумать
    @Autowired
    private HttpServletRequest request;

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

    @Override
    @Transactional
    public CartDto getCart() {
        Cookie[] cookies = request.getCookies();
        List<Long> productIdFromCookie = new ArrayList<>();
        List<Integer> productCountFromCookie = new ArrayList<>();
        Map<Long, Integer> productIdAndCountFromCookie = new HashMap<>();

        if (cookies != null) {
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
            List<ProductDtoCart> productDtoCart = from(cart);


            return CartDto.builder()
                    .productDtoCart(productDtoCart)
                    .sumOfProducts(getSumOfProducts(productDtoCart, cart))
                    .build();
        } else {
            List<Integer> finalProductCountFromCookie = productCountFromCookie;
            List<Long> finalProductIdFromCookie = productIdFromCookie;
            List<Cart> cart = IntStream.range(0, productIdFromCookie.size())
                    .mapToObj(i -> Cart.builder()
                            .product(getOrThrow.getProductOrThrow(finalProductIdFromCookie.get(i), productsRepository))
                                    .count(finalProductCountFromCookie.get(i))
                            .build()).collect(Collectors.toList());
            List<ProductDtoCart> productDtoCart = from(cart);

            return CartDto.builder()
                    .productDtoCart(productDtoCart)
                    .sumOfProducts(getSumOfProducts(productDtoCart, cart))
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
        }
        else if (count == 0) {
            deleteCart(productId);
        } else {
            cart.setCount(count);
            cartRepository.save(cart);
        }
    }

    private Double getSumOfProducts(List<ProductDtoCart> productDtoCart, List<Cart> cart) {
        Map<String, Float> productsPrices = productDtoCart.stream()
                .collect(Collectors.toMap(ProductDtoCart::getName, ProductDtoCart::getPrice));

        return cart.stream().mapToDouble(i ->
                        (productsPrices.getOrDefault(i.getProduct().getName(), 0.0F) * i.getCount()))
                .sum();
    }

}

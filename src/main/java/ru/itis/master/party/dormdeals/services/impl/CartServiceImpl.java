package ru.itis.master.party.dormdeals.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.utils.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartProductConverter cartProductConverter;

    private final UserUtil userUtil;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ProductsRepository productsRepository;

    @Override
    public void addCart(Long productId) {
        User user = userUtil.initThisUser(userRepository);
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId).orElse(null);

        if (product.getState().equals(Product.State.ACTIVE)) {
            if (cart != null && product.getCountInStorage() == cart.getCount()) {
                throw new NotEnoughException(Product.class, cart.getCount(), (int) product.getCountInStorage());
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
    //TODO сделать проверку на количество в куках и количество на складе, но это мб будет делать фронт
    @Override
    @Transactional
    public CartDto getCart(String cookieHeader) {
        List<Long> productIdFromCookie = new ArrayList<>();
        List<Integer> productCountFromCookie= new ArrayList<>();
        Map<Long, Integer> productIdAndCountFromCookie = new HashMap<>();

        if (cookieHeader != null) {
            List<Cookie> cookies = getCookieFromHeader(cookieHeader);

            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "cart" -> productIdFromCookie = Arrays.stream(cookie.getValue().split("&"))
                            .map(Long::parseLong).collect(Collectors.toList());
                    case "count" -> productCountFromCookie = Arrays.stream(cookie.getValue().split("&"))
                            .map(Integer::parseInt).collect(Collectors.toList());
                }
            }

            productIdAndCountFromCookie = IntStream.range(0, productIdFromCookie.size())
                    .boxed()
                    .collect(Collectors.toMap(productIdFromCookie::get, productCountFromCookie::get));
        }

        try {
            User user = userUtil.initThisUser(userRepository);
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

        } catch (NotAcceptableException ex) {
            List<Integer> finalProductCountFromCookie = productCountFromCookie;
            List<Long> finalProductIdFromCookie = productIdFromCookie;
            List<Cart> cart = IntStream.range(0, productIdFromCookie.size())
                    .mapToObj(i -> {
                        Long productId = finalProductIdFromCookie.get(i);
                        return Cart.builder()
                                .product(productsRepository.findById(productId).orElseThrow(
                                        () -> new NotFoundException(Product.class, "id", productId)))
                                .count(finalProductCountFromCookie.get(i))
                                .build();
                    }).collect(Collectors.toList());
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
        User user = userUtil.initThisUser(userRepository);
        cartRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    @Override
    public void setCountProduct(Long productId, Integer count) {
        User user = userUtil.initThisUser(userRepository);
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (count > product.getCountInStorage()) {
            throw new NotEnoughException(Product.class, count, (int) product.getCountInStorage());
        } else if (count == 0) {
            deleteCart(productId);
        } else {
            cart.setCount(count);
            cartRepository.save(cart);
        }
    }

    private Double getSumOfProducts(List<CartProductDto> cartProductDto, List<Cart> cart) {
        return cart.stream()
                .mapToDouble(c -> cartProductDto.stream()
                        .filter(cp -> cp.getName().equals(c.getProduct().getName()))
                        .findFirst()
                        .map(CartProductDto::getPrice)
                        .orElse(0.0F) * c.getCount())
                .reduce(0.0, Double::sum);
    }

    private List<Cookie> getCookieFromHeader(String cookieHeader) {
        return Arrays.stream(cookieHeader.split(";\\s*"))
                .map(s -> new Cookie(s.split("=", 2)[0], s.split("=", 2)[1]))
                .collect(Collectors.toList());
    }

}

package ru.itis.master.party.dormdeals.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.CartDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.CartProductDto;
import ru.itis.master.party.dormdeals.dto.converters.CartProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.CartService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartProductConverter cartProductConverter;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ProductsRepository productsRepository;

    @Override
    public void addCart(long userId, Long productId) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndProductId(userId, productId);

        if (product.getState().equals(Product.State.ACTIVE)) {
            if (cartOptional.isEmpty()) {
                cartRepository.save(Cart.builder()
                        .user(userRepository.getReferenceById(userId))
                        .product(product)
                        .count(1)
                        .state(Cart.State.ACTIVE)
                        .build());
            }
        }
    }

    //TODO сделать проверку на количество в куках и количество на складе
    @Override
    @Transactional
    public CartDto getCart(String cookieHeader) {
        Map<Long, Integer> productIdAndCount = getMapOfProductIdAndCount(cookieHeader);
        List<Cart> cart = productIdAndCount.entrySet().stream()
                .map(entry -> Cart.builder()
                        .product(productsRepository.getReferenceById(entry.getKey()))
                        .count(entry.getValue())
                        .build())
                .toList();

        List<CartProductDto> cartProductDto = cartProductConverter.from(cart);
        return CartDto.builder()
                .cartProductDto(cartProductDto)
                .sumOfProducts(getSumOfProducts(cartProductDto, cart))
                .build();
    }

    @Override
    public CartDto getCart(long userId, String cookieHeader) {
        Map<Long, Integer> productIdAndCount = getMapOfProductIdAndCount(cookieHeader);
        if (productIdAndCount.size() > 0) {
            productIdAndCount.forEach(((productId, count) -> {
                addCart(userId, productId);
                setCountProduct(userId, productId, count);
            }));
        }
        List<Cart> cart = cartRepository.findByUserId(userId);
        List<CartProductDto> cartProductDto = cartProductConverter.from(cart);

        return CartDto.builder()
                .cartProductDto(cartProductDto)
                .sumOfProducts(getSumOfProducts(cartProductDto, cart))
                .build();
    }


    //TODO обработать случаи когда длины списков не равны
    private Map<Long, Integer> getMapOfProductIdAndCount(String cookieHeader) {
        if (cookieHeader == null) {
            return Map.of();
        }
        List<Cookie> cookies = getCookieFromHeader(cookieHeader);
        List<Long> productIdFromCookie = List.of();
        List<Integer> productCountFromCookie = List.of();

        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "cart" -> productIdFromCookie = Arrays.stream(cookie.getValue().split("&"))
                        .map(Long::parseLong).collect(Collectors.toList());
                case "count" -> productCountFromCookie = Arrays.stream(cookie.getValue().split("&"))
                        .map(Integer::parseInt).collect(Collectors.toList());
            }
        }
        return IntStream.range(0, productIdFromCookie.size())
                .boxed()
                .collect(Collectors.toMap(productIdFromCookie::get, productCountFromCookie::get));
    }


    @Transactional
    @Override
    public void deleteCart(long userId, Long productId) {
        User user = userRepository.getReferenceById(userId);
        cartRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    @Override
    public void setCountProduct(long userId, Long productId, Integer count) {
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, "id", productId));

        if (count > product.getCountInStorage()) {
            throw new NotEnoughException(Product.class, count, (int) product.getCountInStorage());
        } else if (count == 0) {
            deleteCart(userId, productId);
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

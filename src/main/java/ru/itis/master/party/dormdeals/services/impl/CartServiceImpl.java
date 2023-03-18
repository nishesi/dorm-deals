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

import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDtoCart.from;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OwnerChecker ownerChecker;
    private final GetOrThrow getOrThrow;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void addCart(Long productId) {
        User user = ownerChecker.initThisUser(userRepository);
        Product product = getOrThrow.getProductOrThrow(productId, productsRepository);
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId);


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
    public CartDto getCart() {
        Cookie[] cookies = request.getCookies();
        List<Long> productIdFromCookie = new ArrayList<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    productIdFromCookie = Arrays.stream(cookie.getValue().split("&")).map(Long::parseLong).collect(Collectors.toList());
                }
            }
            productIdFromCookie.forEach(this::addCart);
        }



        User user = ownerChecker.initThisUser(userRepository);
        List<Cart> cart = cartRepository.findByUserId(user.getId());
        List<ProductDtoCart> productDto = from(cart);

        //TODO написать реализацию получения суммы товаров по другому более красиво

        Map<String, Float> productsPrices = productDto.stream()
                .collect(Collectors.toMap(ProductDtoCart::getName, ProductDtoCart::getPrice));

        Double totalCost = cart.stream().mapToDouble(i ->
                (productsPrices.getOrDefault(i.getProduct().getName(), 0.0F) * i.getCount()))
                .sum();


        return CartDto.builder()
                .productDtoCart(productDto)
                .sumOfProducts(totalCost)
                .build();
    }

    @Transactional
    @Override
    public void deleteCart(Long productId) {
        User user = ownerChecker.initThisUser(userRepository);
        cartRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }
}

package ru.itis.master.party.dormdeals.utils;

import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;

@Component
public class GetOrThrow {
    public Product getProductOrThrow(Long productId, ProductsRepository productsRepository) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Товар с идентификатором <" + productId + "> не найден"));
    }
    public Shop getShopOrThrow(Long shopId, ShopsRepository shopsRepository) {
        return shopsRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Магазин с идентификатором <" + shopId + "> не найден"));
    }
    public Cart getCartOrThrow(Long userId, Long productId, CartRepository cartRepository) {
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException("Товара с идентификатором <" + productId + "> нет в корзине"));
    }
}

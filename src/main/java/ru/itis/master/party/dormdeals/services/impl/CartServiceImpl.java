package ru.itis.master.party.dormdeals.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.exceptions.NotEnoughProductException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.CartService;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;
import ru.itis.master.party.dormdeals.utils.GetOrThrow;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OwnerChecker ownerChecker;
    private final GetOrThrow getOrThrow;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;

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
                        .product(getOrThrow.getProductOrThrow(productId, productsRepository))
                        .count(1)
                        .build());
            }
        }
    }

}

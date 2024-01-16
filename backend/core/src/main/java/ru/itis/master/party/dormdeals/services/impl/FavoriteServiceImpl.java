package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.product.ProductDto;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.repositories.jpa.ProductRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.UserRepository;
import ru.itis.master.party.dormdeals.services.FavoriteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final ProductConverter productConverter;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public void addFavorite(long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        if (user.getFavorites().size() >= 25) {
            throw new NotAcceptableException("Максимум 25 товаров в избранном");
        }

        user.getFavorites().add(productRepository.getReferenceById(productId));

        userRepository.save(user);
    }

    @Override
    public List<ProductDto> getFavorites(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        return productConverter.convertListProductInListProductDto(user.getFavorites());
    }

    @Transactional
    @Override
    public void deleteFavorite(long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.getFavorites().removeIf(favoriteProduct -> favoriteProduct.getId().equals(productId));

        userRepository.save(user);
    }
}

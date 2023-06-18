package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.FavoriteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final ProductConverter productConverter;

    private final ProductsRepository productsRepository;

    private final UserRepository userRepository;

    @Override
    public void addFavorite(long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        if (user.getFavorites().size() >= 25) {
            throw new NotAcceptableException("Максимум 25 товаров в избранном");
        }
        user.getFavorites().add(productsRepository.getReferenceById(productId));

        userRepository.save(user);
    }

    @Override
    public List<ProductDto> getFavorites(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        List<Product> products = user.getFavorites();
        return productConverter.from(products);
    }

    @Transactional
    @Override
    public void deleteFavorite(long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.getFavorites().remove(productsRepository.getReferenceById(productId));

        userRepository.save(user);
    }
}

package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.converters.ProductConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.FavoriteService;
import ru.itis.master.party.dormdeals.utils.UserUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final ProductConverter productConverter;

    private final ProductsRepository productsRepository;

    private final UserRepository userRepository;

    private final UserUtil userUtil;

    @Override
    public void addFavorite(Long productId) {
        User user = userUtil.initThisUser(userRepository);

        if (user.getFavorites().size() >= 25) {
            throw new NotAcceptableException("Максимум 25 товаров в избранном");
        }

        user.getFavorites().add(productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("not found")));

        userRepository.save(user);
    }

    @Override
    public List<ProductDto> getFavorites() {
        User user = userUtil.initThisUser(userRepository);
        List<Product> products = user.getFavorites();
        return productConverter.from(products);
    }

    @Transactional
    @Override
    public void deleteFavorite(Long productId) {
        User user = userUtil.initThisUser(userRepository);

        user.getFavorites().remove(productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("not found")));

        userRepository.save(user);
    }
}

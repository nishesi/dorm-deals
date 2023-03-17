package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.models.Favourites;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.FavouriteRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.FavouriteService;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;
import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto.from;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final OwnerChecker ownerChecker;

    @Override
    public void addFavourite(Long productId) {

        favouriteRepository.save(Favourites.builder()
                .user(ownerChecker.initThisUser(userRepository))
                .product(productsRepository.findById(productId).orElseThrow())
                .count(1)
                .build());
    }

    @Override
    public List<ProductDto> getFavourites() {
        User user = ownerChecker.initThisUser(userRepository);
        List<Product> products = favouriteRepository.findByUserId(user.getId()).stream().map(Favourites::getProduct).collect(Collectors.toList());
        return from(products);
    }

    @Transactional
    @Override
    public void deleteFavourite(Long productId) {
        User user = ownerChecker.initThisUser(userRepository);
        favouriteRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }
}
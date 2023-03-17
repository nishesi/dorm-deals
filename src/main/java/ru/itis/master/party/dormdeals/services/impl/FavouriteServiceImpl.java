package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.models.Favourites;
import ru.itis.master.party.dormdeals.repositories.FavouriteRepository;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.FavouriteService;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;

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
                .userId(ownerChecker.initThisUser(userRepository))
                .productId(productsRepository.findById(productId).orElseThrow())
                .build());
    }
}

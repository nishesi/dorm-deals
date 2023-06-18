package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

import java.util.List;

public interface FavoriteService {
    void addFavorite(long userId, Long productId);
    List<ProductDto> getFavorites(long userId);

    void deleteFavorite(long userId, Long productId);
}

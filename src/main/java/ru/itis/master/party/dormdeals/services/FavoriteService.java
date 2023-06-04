package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long productId);
    List<ProductDto> getFavorites();

    void deleteFavorite(Long productId);
}

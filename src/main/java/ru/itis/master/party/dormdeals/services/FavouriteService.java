package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;

import java.util.List;

public interface FavouriteService {
    void addFavourite(Long productId);
    List<ProductDto> getFavourites();

    void deleteFavourite(Long productId);
}

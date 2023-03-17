package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.Favourites;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourites, Long> {
    List<Favourites> findAllByUserId(Long id);
}

package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.Favourites;

public interface FavouriteRepository extends JpaRepository<Favourites, Long> {

}

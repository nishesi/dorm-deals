package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.Token;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {
}

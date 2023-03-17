package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itis.master.party.dormdeals.models.User;

import java.util.Optional;

public interface UserRepository  extends CrudRepository<User, Long> {
    Optional<User> getByEmail(String email);
    Optional<User> getByHashForConfirm(String hashForConfirm);
    boolean existsUserByEmail(String email);
}

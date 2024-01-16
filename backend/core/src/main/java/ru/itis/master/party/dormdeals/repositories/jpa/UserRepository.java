package ru.itis.master.party.dormdeals.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.master.party.dormdeals.models.jpa.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);
    Optional<User> getByHashForConfirm(String hashForConfirm);
    boolean existsUserByEmail(String email);
    void deleteByStateEquals(User.State state);
}

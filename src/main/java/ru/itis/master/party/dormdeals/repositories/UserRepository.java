package ru.itis.master.party.dormdeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.master.party.dormdeals.models.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);
    Optional<User> getByHashForConfirm(String hashForConfirm);
    boolean existsUserByEmail(String email);
    void deleteByStateEquals(User.State state);
    @Query("select u.resource from users u where u.id=:id")
    String getResourceById(Long id);
}

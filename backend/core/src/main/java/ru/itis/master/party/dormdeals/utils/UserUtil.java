package ru.itis.master.party.dormdeals.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

import java.util.Objects;


@Component
public class UserUtil {
    public void checkShopOwner(Long ownerShopId, User thisUser) {
        if (!Objects.equals(thisUser.getId(), ownerShopId)) {
            throw new NotAcceptableException("Вы не являетесь владельцем данного магазина.");
        }
    }
    public User initThisUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return userRepository.getByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException(User.class, "email", authentication.getName()));
        }
        throw new NotAcceptableException("user not authenticated");
    }

    public void checkOrderOwner(Long ownerOrderId, User thisUser) {
        if (!Objects.equals(thisUser.getId(), ownerOrderId)) {
            throw new NotAcceptableException("Вы не являетесь создателем данного заказа.");
        }
    }
}

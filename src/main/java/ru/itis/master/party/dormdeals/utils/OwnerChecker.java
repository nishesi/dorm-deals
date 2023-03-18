package ru.itis.master.party.dormdeals.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotAllowedException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

import java.util.Objects;


@Component
public class OwnerChecker {
    public void checkOwnerShop(Long ownerShopId, User thisUser) {
        if (!Objects.equals(thisUser.getId(), ownerShopId)) {
            throw new NotAllowedException("Вы не являетесь владельцем данного магазина.");
        }
    }
    public User initThisUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void checkOwnerOrder(Long ownerOrderId, User thisUser) {
        if (!Objects.equals(thisUser.getId(), ownerOrderId)) {
            throw new NotAllowedException("Вы не являетесь создателем данного заказа.");
        }
    }
}

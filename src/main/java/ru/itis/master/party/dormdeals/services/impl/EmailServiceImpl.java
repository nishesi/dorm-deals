package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.EmailService;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    @Override
    public void confirmAccount(String hashForConfirm) {
        User user = userRepository.getByHashForConfirm(hashForConfirm)
                .orElseThrow(() -> new NotFoundException(User.class, "confirm_code", hashForConfirm));

        if (user.getState() != User.State.NOT_CONFIRMED)
            throw new NotAcceptableException("account " + user.getEmail() + " can't be confirmed");
        if (!user.getHashForConfirm().equals(hashForConfirm))
            throw new NotAcceptableException("code " + hashForConfirm + " not valid");

        user.setState(User.State.ACTIVE);

        userRepository.save(user);
        cartRepository.save(Cart.builder()
                .user(user)
                .build());
    }
}

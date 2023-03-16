package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.master.party.dormdeals.exceptions.NotAllowedException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.EmailService;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    @Override
    public void confirmAccount(String email, String code) {
        User user = userRepository.getByEmail(email).orElseThrow();

        if (user.getState() != User.State.NOT_CONFIRMED)
            throw new NotAllowedException("account " + email + " can't be confirmed");
        if (!user.getConfirmCode().equals(code))
            throw new NotAllowedException("code " + code + " not valid");

        user.setState(User.State.ACTIVE);
        user.setConfirmCode(null);

        userRepository.save(user);
    }
}

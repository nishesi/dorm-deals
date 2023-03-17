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
    public void confirmAccount(String hashForConfirm) {
        User user = userRepository.getByHashForConfirm(hashForConfirm).orElseThrow();

        if (user.getState() != User.State.NOT_CONFIRMED)
            throw new NotAllowedException("account " + user.getEmail() + " can't be confirmed");
        if (!user.getHashForConfirm().equals(hashForConfirm))
            throw new NotAllowedException("code " + hashForConfirm + " not valid");

        user.setState(User.State.ACTIVE);

        userRepository.save(user);
    }
}

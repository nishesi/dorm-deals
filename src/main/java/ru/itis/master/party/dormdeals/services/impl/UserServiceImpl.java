package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.user.NewUserDto;
import ru.itis.master.party.dormdeals.dto.user.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.dto.converters.UserConverter;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.Cart;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.CartRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.UserService;
import ru.itis.master.party.dormdeals.utils.EmailUtil;

import java.util.List;
import java.util.UUID;

import static ru.itis.master.party.dormdeals.models.User.State.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final EmailUtil emailUtil;

    @Value("${password.salt}")
    private String salt;

    @Transactional
    public String register(NewUserDto userDto) {
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("User with email = <" + userDto.getEmail() + "> is exist");

        User returnedUser = userRepository.save(User.builder()
                .email(userDto.getEmail())
                .hashPassword(passwordEncoder.encode(userDto.getPassword() + salt))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .telephone(userDto.getTelephone())
                .state(NOT_CONFIRMED)
                .hashForConfirm(DigestUtils.sha256Hex(userDto.getEmail() + UUID.randomUUID()))
                .authorities(List.of(Authority.ROLE_USER))
                .build());

        String confirmationUrl = "http://localhost/email/confirm?accept=" +
                returnedUser.getHashForConfirm();

//        emailUtil.sendMail(userDto.getEmail(),
//                "confirm",
//                "confirmation-mail.ftlh",
//                Map.of("confirmationUrl", confirmationUrl));

        return "Please, check your email to confirm account.";
    }

    public UserDto getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));
        return userConverter.from(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UpdateUserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.setHashPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setTelephone(userDto.getTelephone());

        return userConverter.from(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.setState(DELETED);
    }

    @Override
    @Transactional
    public void activateAccount(String hashForConfirm) {
        User user = userRepository.getByHashForConfirm(hashForConfirm)
                .orElseThrow(() -> new NotFoundException(User.class, "confirm_code", hashForConfirm));

        if (user.getState() != NOT_CONFIRMED)
            throw new NotAcceptableException("account " + user.getEmail() + " can't be confirmed");
        if (!user.getHashForConfirm().equals(hashForConfirm))
            throw new NotAcceptableException("code " + hashForConfirm + " not valid");

        user.setState(ACTIVE);

        cartRepository.save(Cart.builder()
                .user(user)
                .build());
    }

    @Override
    @Transactional
    public void deleteUnconfirmedUsers() {
        userRepository.deleteByStateEquals(NOT_CONFIRMED);
    }
}

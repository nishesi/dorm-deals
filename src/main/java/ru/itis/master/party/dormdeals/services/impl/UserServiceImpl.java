package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Role;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.UserService;
import ru.itis.master.party.dormdeals.utils.EmailUtil;
import ru.itis.master.party.dormdeals.utils.OwnerChecker;
import ru.itis.master.party.dormdeals.utils.ResourceUrlResolver;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ResourceUrlResolver resourceUrlResolver;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final OwnerChecker ownerChecker;

    private final EmailUtil emailUtil;

    @Transactional
    public String register(NewUserDto userDto) {
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("User with email = <" + userDto.getEmail() + "> is exist");

        User returnedUser = userRepository.save(User.builder()
                .email(userDto.getEmail())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .telephone(userDto.getTelephone())
                .dormitory(userDto.getDormitory())
                .state(User.State.NOT_CONFIRMED)
                .hashForConfirm(DigestUtils.sha256Hex(userDto.getEmail() + UUID.randomUUID()))
                .role(Role.ROLE_USER)
                .build());

        String confirmationUrl = "http://localhost/email/confirm?accept=" +
                returnedUser.getHashForConfirm();

        emailUtil.sendMail(userDto.getEmail(),
                "confirm",
                "confirmation-mail.ftlh",
                Map.of("confirmationUrl", confirmationUrl));

        return "Please, check your email to confirm account.";
    }

    public UserDto getUser(String email) {
        User user = getUserFromRepository(email);
        return UserDto.from(user, resourceUrlResolver);
    }

    public UserDto updateUser(NewUserDto userDto) {
        User user = ownerChecker.initThisUser(userRepository);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setTelephone(userDto.getTelephone());
        user.setDormitory(userDto.getDormitory());
        user.setHashPassword(passwordEncoder.encode(userDto.getPassword()));

        return UserDto.from(userRepository.save(user), resourceUrlResolver);
    }

    public void deleteUser(String email) {
        User user = getUserFromRepository(email);
        user.setState(User.State.DELETED);
        userRepository.save(user);
    }

    private User getUserFromRepository(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = <" + email + "> is not found"));
    }
}

package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.dto.converters.OrderConverter;
import ru.itis.master.party.dormdeals.dto.converters.UserConverter;
import ru.itis.master.party.dormdeals.dto.orders.OrderDto;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.models.order.Order;
import ru.itis.master.party.dormdeals.repositories.OrderRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final OrderConverter orderConverter;

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
                .state(User.State.NOT_CONFIRMED)
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

    public UserDto updateUser(long userId, UpdateUserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.setHashPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setTelephone(userDto.getTelephone());

        user = userRepository.save(user);
        return userConverter.from(user);
    }

    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.setState(User.State.DELETED);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUnconfirmedUsers() {
        userRepository.deleteByStateEquals(User.State.NOT_CONFIRMED);
    }
}

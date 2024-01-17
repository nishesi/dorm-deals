package ru.itis.master.party.dormdeals.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.user.NewUserForm;
import ru.itis.master.party.dormdeals.dto.user.UpdateUserForm;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.enums.EntityType;
import ru.itis.master.party.dormdeals.enums.FileType;
import ru.itis.master.party.dormdeals.exceptions.NotAcceptableException;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.mapper.UserMapper;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.jpa.Cart;
import ru.itis.master.party.dormdeals.models.jpa.User;
import ru.itis.master.party.dormdeals.repositories.jpa.CartRepository;
import ru.itis.master.party.dormdeals.repositories.jpa.UserRepository;
import ru.itis.master.party.dormdeals.services.ResourceService;
import ru.itis.master.party.dormdeals.services.UserService;
import ru.itis.master.party.dormdeals.utils.EmailUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.itis.master.party.dormdeals.models.jpa.User.State.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ResourceService resourceService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailUtil emailUtil;

    @Value("${app.password.salt}")
    private String salt;

    @Transactional
    public String register(NewUserForm userDto) {
        if (userRepository.existsUserByEmail(userDto.email()))
            throw new IllegalArgumentException("Author with email = <" + userDto.email() + "> is exist");

        User returnedUser = userRepository.save(User.builder()
                .email(userDto.email())
                .hashPassword(passwordEncoder.encode(userDto.password() + salt))
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .telephone(userDto.telephone())
                .state(NOT_CONFIRMED)
                .hashForConfirm(DigestUtils.sha256Hex(userDto.email() + UUID.randomUUID()))
                .authorities(List.of(Authority.ROLE_USER))
                .countUnreadNotifications(0)
                .build());

        String confirmationUrl = "http://localhost/email/confirm?accept=" +
                returnedUser.getHashForConfirm();

        Thread.ofVirtual().start(() -> emailUtil
                .sendMail(userDto.email(),
                        "confirm",
                        "confirmation-mail.ftlh",
                        Map.of("confirmationUrl", confirmationUrl)));

        return "Please, check your email to confirm account.";
    }

    public UserDto getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UpdateUserForm userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, "id", userId));

        user.setHashPassword(passwordEncoder.encode(userDto.password()));
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setTelephone(userDto.telephone());

        return userMapper.toUserDto(user);
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

    @Override
    public void updateUserImage(long id, MultipartFile file) {
        resourceService.saveFile(FileType.IMAGE, EntityType.USER, String.valueOf(id), file);
    }
}

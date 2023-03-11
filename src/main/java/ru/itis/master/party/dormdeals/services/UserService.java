package ru.itis.master.party.dormdeals.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.UserDto;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Authority;
import ru.itis.master.party.dormdeals.models.Role;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDto register(UserDto userDto) {
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("User with email = <" + userDto.getEmail() + "> is exist");

        User returnedUser = userRepository.save(User.builder()
                .email(userDto.getEmail())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role(Role.USER)
                .state(User.State.ACTIVE)
                .telephone(userDto.getTelephone())
                .build());
        return UserDto.from(returnedUser);
    }

    public UserDto getUser(String email) {
        User user = getUserFromRepository(email);
        return UserDto.from(user);
    }

    public UserDto updateUser(UserDto userDto) {
        User updatedUser = getUserFromRepository(userDto.getEmail());
        updatedUser.setHashPassword(userDto.getPassword());
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setTelephone(userDto.getTelephone());

        return UserDto.from(userRepository.save(updatedUser));
    }

    private User getUserFromRepository(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = <" + email + "> is not found"));
    }

    public void deleteUser(String email) {
        User user = getUserFromRepository(email);
        user.setState(User.State.DELETED);
        userRepository.save(user);
    }
}

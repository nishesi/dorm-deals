package ru.itis.master.party.dormdeals.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.UserDto;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto register(UserDto userDto) {
        if (userRepository.existsUserByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("User with email = <" + userDto.getEmail() + "> is exist");

        User returnedUser = userRepository.save(User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
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
        updatedUser.setPassword(userDto.getPassword());
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

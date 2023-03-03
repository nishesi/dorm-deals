package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.UserDto;
import ru.itis.master.party.dormdeals.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public UserDto getUser(String email) {
        return userService.getUser(email);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userService.register(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @Override
    public ResponseEntity<?> deleteUser(String email) {
        userService.deleteUser(email);
        return ResponseEntity.accepted().build();
    }
}

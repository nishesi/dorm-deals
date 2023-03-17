package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.services.UserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public UserDto getUser(String email) {
        return userService.getUser(email);
    }

    @Override
    public ResponseEntity<?> addUser(UserDto userDto) {
        // TODO переписать документацию метода (правильно записать возврат json с одинм параметром message)
        return ResponseEntity.accepted().body(Map.of("message", userService.register(userDto)));
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

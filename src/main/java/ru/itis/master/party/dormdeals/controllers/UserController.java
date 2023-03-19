package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.services.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<?> addUser(NewUserDto userDto) {
        return ResponseEntity.accepted().body(Map.of("message",userService.register(userDto)));
    }

    @Override
    public UserDto getUser(Principal principal) {
        return userService.getUser(principal.getName());
    }

    @Override
    public UserDto updateUser(Principal principal, UpdateUserDto userDto) {
        return userService.updateUser(principal.getName(), userDto);
    }

    @Override
    public ResponseEntity<?> deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
        return ResponseEntity.accepted().build();
    }
}

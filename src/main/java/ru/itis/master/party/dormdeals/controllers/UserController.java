package ru.itis.master.party.dormdeals.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.exceptions.NotAllowedException;
import ru.itis.master.party.dormdeals.services.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public UserDto getUser(Principal principal) {
        return userService.getUser(principal.getName());
    }

    @Override
    public ResponseEntity<?> addUser(NewUserDto userDto) {
        //TODO переписать документацию метода (правильно записать возврат json с одинм параметром message)
        return ResponseEntity.accepted().body(Map.of("message",userService.register(userDto)));
    }

    @Override
    public UserDto updateUser(NewUserDto userDto, Principal principal) {
        if (principal.getName().equals(userDto.getEmail()))
            throw new NotAllowedException("Not allowed to update");
        return userService.updateUser(userDto);
    }

    @Override
    public ResponseEntity<?> deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
        return ResponseEntity.accepted().build();
    }
}

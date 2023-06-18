package ru.itis.master.party.dormdeals.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.MessageDto;
import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<?> addUser(@Valid NewUserDto userDto) {
        return ResponseEntity.accepted().body(new MessageDto(userService.register(userDto)));
    }

    @Override
    public UserDto getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUser(userDetails.getUser().getId());
    }

    @Override
    public UserDto updateUser(@Valid UpdateUserDto userDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUser(userDetails.getUser().getId(), userDto);
    }

    @Override
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return ResponseEntity.accepted().build();
    }
}

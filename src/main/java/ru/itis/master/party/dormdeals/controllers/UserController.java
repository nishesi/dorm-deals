package ru.itis.master.party.dormdeals.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.controllers.api.UserApi;
import ru.itis.master.party.dormdeals.dto.MessageDto;
import ru.itis.master.party.dormdeals.dto.user.NewUserDto;
import ru.itis.master.party.dormdeals.dto.user.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.user.UserDto;
import ru.itis.master.party.dormdeals.dto.order.OrderDto;
import ru.itis.master.party.dormdeals.security.details.UserDetailsImpl;
import ru.itis.master.party.dormdeals.services.OrderService;
import ru.itis.master.party.dormdeals.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final OrderService orderService;

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

    @Override
    public ResponseEntity<Page<OrderDto>> getUserOrders(Integer pageInd, Integer pageSize,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(pageInd, pageSize);
        Page<OrderDto> userOrders = orderService.getUserOrders(userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(userOrders);
    }

    @Override
    public ResponseEntity<?> updateUserImage(MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateUserImage(userDetails.getUser().getId(), file);
        return ResponseEntity.accepted().build();
    }
}

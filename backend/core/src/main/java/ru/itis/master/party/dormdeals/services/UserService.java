package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.user.NewUserDto;
import ru.itis.master.party.dormdeals.dto.user.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.user.UserDto;

public interface UserService {
    String register(NewUserDto userDto);

    UserDto getUser(long userId);

    UserDto updateUser(long userId, UpdateUserDto userDto);

    void deleteUser(long userId);

    void activateAccount(String hashEmail);

    void deleteUnconfirmedUsers();

    void updateUserImage(long id, MultipartFile file);
}

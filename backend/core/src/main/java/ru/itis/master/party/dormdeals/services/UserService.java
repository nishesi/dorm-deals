package ru.itis.master.party.dormdeals.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.dto.user.NewUserForm;
import ru.itis.master.party.dormdeals.dto.user.UpdateUserForm;
import ru.itis.master.party.dormdeals.dto.user.UserDto;

public interface UserService {
    String register(NewUserForm userDto);

    UserDto getUser(long userId);

    UserDto updateUser(long userId, UpdateUserForm userDto);

    void deleteUser(long userId);

    void activateAccount(String hashEmail);

    void deleteUnconfirmedUsers();

    void updateUserImage(long id, MultipartFile file);
}

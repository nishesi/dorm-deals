package ru.itis.master.party.dormdeals.services.UserServices;

import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;

public interface UserService {
    UserDto register(UserDto userDto);
    UserDto getUser(String email);
    UserDto updateUser(UserDto userDto);
    void deleteUser(String email);
}

package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;

public interface UserService {
    String register(NewUserDto userDto);

    UserDto getUser(long userId);

    UserDto updateUser(long userId, UpdateUserDto userDto);

    void deleteUser(long userId);

    void deleteUnconfirmedUsers();
}

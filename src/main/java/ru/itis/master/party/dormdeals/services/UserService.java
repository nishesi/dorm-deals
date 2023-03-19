package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.UserDto.NewUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UpdateUserDto;
import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;

public interface UserService {
    String register(NewUserDto userDto);
    UserDto getUser(String email);
    UserDto updateUser(String email, UpdateUserDto userDto);
    void deleteUser(String email);
    void deleteUnconfirmedUsers();
}

package ru.itis.master.party.dormdeals.services;

import ru.itis.master.party.dormdeals.dto.UserDto.UserDto;

public interface UserService {
    String register(UserDto userDto);
    UserDto getUser(String email);
    UserDto updateUser(UserDto userDto);
    void deleteUser(String email);
    void deleteUnconfirmedUsers();
}

package ru.practicum.service.userService;

import ru.practicum.dto.userDto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    UserDto addUser(UserDto userDto);

    void deleteUser(Integer userId);
}

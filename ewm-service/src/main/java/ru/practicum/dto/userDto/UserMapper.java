package ru.practicum.dto.userDto;

import ru.practicum.model.User;

public class UserMapper {
    public static UserDto toUserDtoFromUser(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static User toUserFromUserDto(UserDto userDto) {
        return new User(null,
                userDto.getEmail(),
                userDto.getName());
    }

    public static UserShotDto userShotDtoFromUser(User user) {
        return new UserShotDto(user.getId(), user.getName());
    }

}

package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User dtoToUser(UserDto userDto) {
      /*  return new User(userDto.getId(), userDto.getEmail(), userDto.getName());*/
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto userToDto(User user) {
      //  return new UserDto(user.getId(), user.getEmail(), user.getName());
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

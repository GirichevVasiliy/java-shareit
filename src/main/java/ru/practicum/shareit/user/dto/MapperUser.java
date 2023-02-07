package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class MapperUser {
    public static User dtoToUser(UserDto userDto){
        return new User(userDto.getId(), userDto.getEmail(), userDto.getName());
    }
    public static UserDto userToDto(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

}

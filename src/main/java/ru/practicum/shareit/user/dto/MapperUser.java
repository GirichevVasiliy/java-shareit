package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class MapperUser {
    public static User dtoToUser(UserDto userDto){
        return User.builder().id(userDto.getId()).email(userDto.getEmail()).name(userDto.getName()).build();
    }
    public static UserDto userToDto(User user){
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}

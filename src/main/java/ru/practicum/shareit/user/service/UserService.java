package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    List<User> getAllUsers();
}

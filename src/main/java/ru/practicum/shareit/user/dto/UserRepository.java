package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User addUser(User user);
    User updateUser(User user);
    Optional<User> getUserById(Long userId);
    void deleteUserById(Long userId);
    List<User>getAllUsers();
}

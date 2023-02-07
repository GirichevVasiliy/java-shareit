package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.RequestError;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private Long userId = 1L;

    private Long getNextId() {
        return userId++;
    }

    @Override
    public User addUser(User user) {
        Long id = getNextId();
        User newUser = user.withId(id);
        users.put(newUser.getId(), newUser);
        log.info("Пользователь с ID " + user.getId() + " добавлен");
        return newUser;
    }

    @Override
    public User updateUser(User user, Long userId) {
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return user;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}

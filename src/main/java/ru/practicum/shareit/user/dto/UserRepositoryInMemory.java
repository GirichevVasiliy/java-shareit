package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.RequestError;
import ru.practicum.shareit.user.model.User;

import java.util.*;
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
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с ID " + user.getId() + " не найден");
        }
    }

    // Заготовка для работы с БД
    @Override
    public Optional<User> getUserById(Long userId) {
        try {
            Optional<User> user = Optional.ofNullable(users.get(userId));
            return user.stream().findAny();
        } catch (NullPointerException e) {
            return Optional.empty();
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

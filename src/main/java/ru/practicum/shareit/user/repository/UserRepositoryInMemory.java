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
        if (!checkEmailDuplicate(user)) {
            Long id = getNextId();
            user.setId(id);
            users.put(user.getId(), user);
            log.info("Пользователь с ID " + user.getId() + " добавлен");
            return users.get(id);
        } else {
            throw new RequestError(HttpStatus.CONFLICT, "Пользователь с Email " + user.getEmail() + " не может быть добавлен");
        }
    }

    @Override
    public User updateUser(User user, Long userId) {
        if (!users.values().stream().filter(u -> u.getId() != userId).map(User::getEmail).anyMatch(e -> e.equals(user.getEmail()))) {
            if (users.containsKey(userId)) {
                user.setId(userId);
                if (user.getName() == null) {
                    user.setName(users.get(userId).getName());
                }
                if (user.getEmail() == null) {
                    user.setEmail(users.get(userId).getEmail());
                }
                users.put(userId, user);
                return users.get(userId);
            } else {
                throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с ID " + user.getEmail() + " не найден");
            }
        } else {
            throw new RequestError(HttpStatus.CONFLICT, "Пользователь ID " + userId + " пытался обновить Email " + user.getEmail() + " данный Email уже занят");
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

    private boolean checkEmailDuplicate(User user) {
        return users.values().stream().map(User::getEmail).anyMatch(u -> u.equals(user.getEmail()));
    }
}

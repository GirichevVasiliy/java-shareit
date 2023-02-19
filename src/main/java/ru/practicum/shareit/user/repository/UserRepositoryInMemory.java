package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private Long id = 1L;

    private Long getNextId() {
        return id++;
    }

    @Override
    public User addUser(User user) {
        Long idUser = getNextId();
        user.setId(idUser);
        users.put(user.getId(), user);
        log.info("Пользователь с ID " + user.getId() + " добавлен");
        return users.get(idUser);
    }

    @Override
    public User updateUser(User user, Long userId) {
        user.setId(userId);
        if (user.getName() == null) {
            user.setName(users.get(userId).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(users.get(userId).getEmail());
        }
        users.put(userId, user);
        return users.get(userId);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}

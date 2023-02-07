package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public class UserDBRepository implements UserRepository {
    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user, Long userId) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public void deleteUserById(Long userId) {
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}

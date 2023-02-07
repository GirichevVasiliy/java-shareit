package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
@Repository
public class UserDBRepository implements UserRepository{
    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.empty();
    }

    @Override
    public void deleteUserById(Long userId) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}

package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepositoryInMemory") UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public User addUser(User user) {
        log.info("Получен запрос на добавление пользователя " + user.getEmail());
        return userRepository.addUser(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        log.info("Получен запрос на обновление пользователя " + user.getId());
        return userRepository.updateUser(user, userId);
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Получен запрос на поиск пользователя по ID" + userId);
        return userRepository.getUserById(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Получен запрос на удаление пользователя по ID" + userId);
        userRepository.deleteUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userRepository.getAllUsers();
    }
}

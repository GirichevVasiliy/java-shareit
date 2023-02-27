package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.InvalidOwnerException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepository") UserRepository repository) {
        this.userRepository = repository;
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Получен запрос на добавление пользователя " + userDto.getEmail());
        User user = UserMapper.dtoToUser(userDto);
        if (!checkEmailDuplicate(user)) {
            UserMapper.userToDto(userRepository.save(user));
            Optional<User> us = userRepository.findById(1L);
            return UserMapper.userToDto(us.get());
        } else {
            throw new InvalidOwnerException("Пользователь с Email " + user.getEmail() + " не может быть добавлен");
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Получен запрос на обновление пользователя " + userId);
        User user = UserMapper.dtoToUser(userDto);
        List<User> allUsers = userRepository.findAll();
        if (!allUsers.stream().filter(u -> !u.getId().equals(userId)).map(User::getEmail).anyMatch(e -> e.equals(userDto.getEmail()))) {
            if (allUsers.stream().anyMatch(u -> u.getId().equals(userId))) {
                return UserMapper.userToDto(userRepository.save(user));
            } else {
                throw new ResourceNotFoundException("Пользователь с ID " + user.getEmail() + " не найден");
            }
        } else {
            throw new InvalidOwnerException("Пользователь ID " + userId + " пытался обновить Email " +
                    user.getEmail() + " данный Email уже занят");
        }
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Получен запрос на поиск пользователя по ID" + userId);
        if (userRepository.findById(userId).isPresent()) {
            return UserMapper.userToDto(userRepository.findById(userId).get());
        } else {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        log.info("Получен запрос на удаление пользователя по ID" + userId);
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }

    private boolean checkEmailDuplicate(User user) {
        return userRepository.findAll().stream().map(User::getEmail).anyMatch(u -> u.equals(user.getEmail()));
    }
}

package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.MapperUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepositoryInMemory") UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Получен запрос на добавление пользователя " + userDto.getEmail());
        User user = MapperUser.dtoToUser(userDto);
        if (!checkEmailDuplicate(user)) {
            return MapperUser.userToDto(userRepository.addUser(user));
        } else {
            throw new InvalidOwnerException("Пользователь с Email " + user.getEmail() + " не может быть добавлен");
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Получен запрос на обновление пользователя " + userId);
        User user = MapperUser.dtoToUser(userDto);
        List<User> allUsers = userRepository.getAllUsers();
        if (!allUsers.stream().filter(u -> !u.getId().equals(userId)).map(User::getEmail).anyMatch(e -> e.equals(userDto.getEmail()))) {
            if (allUsers.stream().anyMatch(u -> u.getId().equals(userId))) {
                return MapperUser.userToDto(userRepository.updateUser(user, userId));
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
        if (userRepository.getUserById(userId).isPresent()) {
            return MapperUser.userToDto(userRepository.getUserById(userId).get());
        } else {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Получен запрос на удаление пользователя по ID" + userId);
        if (userRepository.getUserById(userId).isPresent()) {
            userRepository.deleteUserById(userId);
        } else {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userRepository.getAllUsers().stream().map(MapperUser::userToDto).collect(Collectors.toList());
    }

    private boolean checkEmailDuplicate(User user) {
        return userRepository.getAllUsers().stream().map(User::getEmail).anyMatch(u -> u.equals(user.getEmail()));
    }
}

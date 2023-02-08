package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
        return MapperUser.userToDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Получен запрос на обновление пользователя " + userId);
        User user = MapperUser.dtoToUser(userDto);
        return MapperUser.userToDto(userRepository.updateUser(user, userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Получен запрос на поиск пользователя по ID" + userId);
        return MapperUser.userToDto(userRepository.getUserById(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Получен запрос на удаление пользователя по ID" + userId);
        userRepository.deleteUserById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userRepository.getAllUsers().stream().map(MapperUser::userToDto).collect(Collectors.toList());
    }
}

package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    private User user;
    private UserDto userDto;
    final Long userId1 = 1L;

    @BeforeEach
    private void init() {
        user = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
    }
    @Test
    void addUser_whenСorrectUser_thenReturnUserDto() {
        when(userRepository.save(user)).thenReturn(user);
        UserDto newUserDto = userService.addUser(userDto);
        verify(userRepository, times(1)).save(any());
        assertThat(newUserDto.equals(userDto)).isTrue();
    }
    @Test
    void updateUser_whenUserIsNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(userDto, userId1));
        verify(userRepository, never()).save(any());
    }
    @Test
    void updateUser_whenСorrectUser_thenReturnUserDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserDto newUserDto = userService.updateUser(userDto, userId1);
        verify(userRepository, times(1)).save(any());
        assertThat(newUserDto.equals(userDto)).isTrue();
    }
    @Test
    void getUserById_whenUserIsNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(userId1));
    }
    @Test
    void getUserById_whenUserIsFound_thenReturnUserDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        UserDto newUser = userService.getUserById(userId1);
        assertThat(newUser.equals(userDto)).isTrue();
    }
    @Test
    void deleteUserById_whenUserIsFound_void() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        userService.deleteUserById(userId1);
        verify(userRepository, times(1)).deleteById(userId1);
    }
    @Test
    void deleteUserById_whenUserIsFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUserById(userId1));
        verify(userRepository, never()).deleteById(userId1);
    }
    @Test
    void getAllUsers_whenUserDtoListNotEmpty() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<UserDto> userDtoList = userService.getAllUsers();
        assertThat(userDtoList.contains(userDto)).isTrue();
    }
    @Test
    void getAllUsers_whenUserDtoListEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<UserDto> userDtoList = userService.getAllUsers();
        assertThat(userDtoList.isEmpty()).isTrue();
    }
}
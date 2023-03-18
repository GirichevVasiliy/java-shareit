package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMapperTest {
    private User user;
    private UserDto userDto;

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
    @DisplayName("Тест создания User, валидный userDto")
    void dtoToUserValidUserDtoTest() {
        User newUser = UserMapper.dtoToUser(userDto);
        assertThat(newUser.getId().equals(userDto.getId())).isTrue();
        assertThat(newUser.getName().equals(userDto.getName())).isTrue();
        assertThat(newUser.getEmail().equals(userDto.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания User, невалидный userDto, id=null")
    void dtoToUserTestUserDtoIdIsNull() {
        userDto = UserDto.builder()
                .id(null)
                .name("user1")
                .email("y1@email.ru")
                .build();
        User newUser = UserMapper.dtoToUser(userDto);
        assertThat(newUser.getId() == null).isTrue();
        assertThat(newUser.getName().equals(userDto.getName())).isTrue();
        assertThat(newUser.getEmail().equals(userDto.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания User, невалидный userDto, name=null")
    void dtoToUserTestUserDtoNameIsNull() {
        userDto = UserDto.builder()
                .id(1L)
                .name(null)
                .email("y1@email.ru")
                .build();
        User newUser = UserMapper.dtoToUser(userDto);
        assertThat(newUser.getId().equals(userDto.getId())).isTrue();
        assertThat(newUser.getName() == null).isTrue();
        assertThat(newUser.getEmail().equals(userDto.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания User, невалидный userDto, email=null")
    void dtoToUserTestUserDtoEmailIsNull() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email(null)
                .build();
        User newUser = UserMapper.dtoToUser(userDto);
        assertThat(newUser.getId().equals(userDto.getId())).isTrue();
        assertThat(newUser.getName().equals(userDto.getName())).isTrue();
        assertThat(newUser.getEmail() == null).isTrue();
    }

    @Test
    @DisplayName("Тест создания userDto, валидный User")
    void userToDtoValidUserTest() {
        UserDto newUserDto = UserMapper.userToDto(user);
        assertThat(newUserDto.getId().equals(user.getId())).isTrue();
        assertThat(newUserDto.getName().equals(user.getName())).isTrue();
        assertThat(newUserDto.getEmail().equals(user.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания userDto, валидный User, id=null")
    void userToDtoTestIdIsNull() {
        user.setId(null);
        UserDto newUserDto = UserMapper.userToDto(user);
        assertThat(newUserDto.getId() == null).isTrue();
        assertThat(newUserDto.getName().equals(user.getName())).isTrue();
        assertThat(newUserDto.getEmail().equals(user.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания userDto, валидный User, name=null")
    void userToDtoTestNameIsNull() {
        user.setName(null);
        UserDto newUserDto = UserMapper.userToDto(user);
        assertThat(newUserDto.getId().equals(user.getId())).isTrue();
        assertThat(newUserDto.getName() == null).isTrue();
        assertThat(newUserDto.getEmail().equals(user.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест создания userDto, валидный User")
    void userToDtotestEmailIsNull() {
        user.setEmail(null);
        UserDto newUserDto = UserMapper.userToDto(user);
        assertThat(newUserDto.getId().equals(user.getId())).isTrue();
        assertThat(newUserDto.getName().equals(user.getName())).isTrue();
        assertThat(newUserDto.getEmail() == null).isTrue();
    }
}
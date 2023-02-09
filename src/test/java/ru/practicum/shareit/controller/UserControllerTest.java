package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.error.RequestError;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryInMemory;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Тест пользователя")
class UserControllerTest {
    private UserController userController;

    @BeforeEach
    private void init() {
        userController = new UserController(new UserServiceImpl(new UserRepositoryInMemory()));
    }

    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userDto3;

    @BeforeEach
    private void initUserDto() {
        userDto1 = new UserDto(1L, "user1@email.mail", "user1-name");
        userDto2 = new UserDto(2L, "user2@email.mail", "user2-name");
        userDto3 = new UserDto(3L, "user3@email.mail", "user3-name");

    }

    @Test
    @DisplayName("Стандартный тест добавления пользователя")
    void addUserTest() {
        assertThat(userDto1).isEqualTo(userController.addUser(userDto1));
    }

    @Test
    @DisplayName("Тест дубликата добавления пользователя")
    void addUserDuplicateTest() {
        assertThat(userDto1).isEqualTo(userController.addUser(userDto1));
        assertThrows(RequestError.class, () -> userController.addUser(userDto1));
        try {
            userController.addUser(userDto1);
        } catch (RequestError e) {
            assertThat(e.getStatus().is4xxClientError()).isTrue();
        }
    }

    @Test
    @DisplayName("Тест обновления имени пользователя")
    void updateNameUserTest() {
        userController.addUser(userDto1);
        userDto1.setName("user1-new-name");
        assertThat(userDto1).isEqualTo(userController.updateUser(1L, userDto1));
    }

    @Test
    @DisplayName("Тест обновления имени пользователя = null")
    void updateNameUserEqualsNullTest() {
        userController.addUser(userDto1);
        assertThat(userDto1).isEqualTo(userController.updateUser(1L, new UserDto(1L, "user1@email.mail", null)));
    }

    @Test
    @DisplayName("Тест обновления почты пользователя")
    void updateEmailUserTest() {
        userController.addUser(userDto1);
        userDto1.setEmail("g-user1@email.com");
        assertThat(userDto1).isEqualTo(userController.updateUser(1L, userDto1));
    }

    @Test
    @DisplayName("Тест обновления почты пользователя = null")
    void updateEmaiUserEqualsNullTest() {
        userController.addUser(userDto1);
        assertThat(userDto1).isEqualTo(userController.updateUser(1L, new UserDto(1L, null, "user1-name")));
    }

    @Test
    @DisplayName("Тест получения списка пользователей")
    void getUsersTest() {
        final int size = 3;
        userController.addUser(userDto1);
        userController.addUser(userDto2);
        userController.addUser(userDto3);
        assertThat(userController.getUsers().size() == size).isTrue();
        assertThat(userController.getUsers().contains(userDto1)).isTrue();
        assertThat(userController.getUsers().contains(userDto2)).isTrue();
        assertThat(userController.getUsers().contains(userDto3)).isTrue();
    }

    @Test
    @DisplayName("Тест получения пустого списка пользователей")
    void getIsEmptyListUsersTest() {
        assertThat(userController.getUsers().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест получения пользователя по ID")
    void getUserByIdTest() {
        final Long id = 1L;
        userController.addUser(userDto1);
        assertThat(userDto1).isEqualTo(userController.getUserById(id));
    }

    @Test
    @DisplayName("Тест получения пользователя по неверному ID")
    void getUserByIdBadIdTest() {
        final Long id = 999L;
        assertThrows(RequestError.class, () -> userController.getUserById(id));
        try {
            userController.getUserById(id);
        } catch (RequestError e) {
            assertThat(e.getStatus().is4xxClientError()).isTrue();
        }
    }

    @Test
    @DisplayName("Тест удаления пользователя по ID")
    void deleteUserTest() {
        final Long id = 1L;
        userController.addUser(userDto1);
        assertThat(userDto1).isEqualTo(userController.getUserById(id));
        userController.deleteUser(id);
        try {
            userController.getUserById(id);
        } catch (RequestError e) {
            assertThat(e.getStatus().is4xxClientError()).isTrue();
        }
    }

    @Test
    @DisplayName("Тест удаления пользователя по неверному ID")
    void deleteUserBadIDTest() {
        final Long id = 999L;
        assertThrows(RequestError.class, () -> userController.deleteUser(id));
        try {
            userController.getUserById(id);
        } catch (RequestError e) {
            assertThat(e.getStatus().is4xxClientError()).isTrue();
        }
    }
}
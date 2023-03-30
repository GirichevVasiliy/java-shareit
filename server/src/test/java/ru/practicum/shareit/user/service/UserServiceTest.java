package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    UserServiceImpl userService;
    private UserDto userDto;
    final Long userId = 1L;


    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y@email.ru")
                .build();
    }

    @Test
    public void userIntegrationTest() {
        User user = new User("y@email.ru", "user1");
        UserDto saveUserDto = userService.addUser(UserMapper.userToDto(user));
        assertThat(saveUserDto.equals(userDto)).isTrue();

        UserDto newUserDto = userService.getUserById(userId);
        assertThat(newUserDto.equals(userDto)).isTrue();

        User updateUser = new User("userNew", "y@NEWemail.ru");
        UserDto updateUserDto = userService.updateUser(UserMapper.userToDto(updateUser), userId);
        assertThat(updateUserDto.getId().equals(userId)).isTrue();
        assertThat(updateUserDto.getName().equals(updateUserDto.getName())).isTrue();
        assertThat(updateUserDto.getEmail().equals(updateUserDto.getEmail())).isTrue();

        User user2 = new User("y@2email.ru", "user2");
        UserDto saveUserDto2 = userService.addUser(UserMapper.userToDto(user2));

        User user3 = new User("y@3email.ru", "user3");
        UserDto saveUserDto3 = userService.addUser(UserMapper.userToDto(user3));

        List<UserDto> userList = userService.getAllUsers();
        assertThat(userList.contains(updateUserDto)).isTrue();
        assertThat(userList.contains(saveUserDto2)).isTrue();
        assertThat(userList.contains(saveUserDto3)).isTrue();

        userService.deleteUserById(userId);
        List<UserDto> userListAfterDelete = userService.getAllUsers();
        assertThat(userListAfterDelete.contains(updateUserDto)).isFalse();
        assertThat(userListAfterDelete.contains(saveUserDto2)).isTrue();
        assertThat(userListAfterDelete.contains(saveUserDto3)).isTrue();

        final Long badIdUser = 99L;
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(badIdUser));
    }
}

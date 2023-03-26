package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    UserServiceImpl userService;
    private UserDto userDto;

    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(4L)
                .name("user4")
                .email("y@4email.ru")
                .build();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetUserById() {
        final Long userId = 4L;
        UserDto newUserDto = userService.getUserById(userId);
        assertThat(newUserDto.equals(userDto));
    }
}

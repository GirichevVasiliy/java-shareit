package ru.practicum.shareit.user.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserTest {
    @Autowired
    private JacksonTester<User> json;

    @Test
    @SneakyThrows
    public void userDtoTest() {
        User user = new User(1L, "ya@ya.ru", "Alex");
        JsonContent<User> result = json.write(user);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ya@ya.ru");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Alex");
    }

    @Test
    @SneakyThrows
    public void secondUserDtoTest() {
        User user = new User("ya@ya.ru", "Alex");
        JsonContent<User> result = json.write(user);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ya@ya.ru");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Alex");
    }
}
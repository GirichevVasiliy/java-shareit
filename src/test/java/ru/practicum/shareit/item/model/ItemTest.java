package ru.practicum.shareit.item.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemTest {
    @Autowired
    private JacksonTester<Item> json;
    private final User user = new User(1L, "user1", "y1@email.ru");

    @Test
    @SneakyThrows
    public void bookingDtoTest() {
        Item item = new Item(1L, "text", "desc", true, user, null);
        JsonContent<Item> result = json.write(item);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
    }
}
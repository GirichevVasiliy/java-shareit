package ru.practicum.shareit.request.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestTest {
    @Autowired
    private JacksonTester<ItemRequest> json;
    private final User user = new User(1L, "user1", "y1@email.ru");
    private static final LocalDateTime CREATE = LocalDateTime.of(2024, 10, 23, 17, 19, 33);

    @Test
    @SneakyThrows
    public void itemRequestDtoTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "desc", user, CREATE);
        JsonContent<ItemRequest> result = json.write(itemRequest);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(1);
    }

}
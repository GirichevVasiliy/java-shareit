package ru.practicum.shareit.item.comment.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentTest {
    @Autowired
    private JacksonTester<Comment> json;
    private static final LocalDateTime CREATE = LocalDateTime.of(2024, 10, 23, 17, 19, 33);
    private final User user = new User(1L, "user1", "y1@email.ru");
    private final Item item = Item.builder()
            .id(1L)
            .name("item1")
            .description("text")
            .available(true)
            .owner(user)
            .request(null)
            .build();

    @Test
    @SneakyThrows
    public void commentTest() {
        final int id = 1;
        Comment comment = new Comment(1L, "text", item, user, CREATE);
        JsonContent<Comment> result = json.write(comment);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(id);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.author").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
    }
}
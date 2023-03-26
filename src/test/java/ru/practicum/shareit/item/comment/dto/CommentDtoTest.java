package ru.practicum.shareit.item.comment.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;
    private static final LocalDateTime CREATE = LocalDateTime.of(2024, 10, 23, 17, 19, 33);
    private final UserDto user = new UserDto(1L, "user1", "y1@email.ru");

    @Test
    @SneakyThrows
    public void commentDtoTest() {
        CommentDto comment = new CommentDto(1L, "text", user.getName(), CREATE);
        String testData = String.format("{\"id\":1,\"text\":\"text\",\"authorName\":\"y1@email.ru\",\"created\"" +
                ":\"2024-10-23T17:19:33.000\"}");
        CommentDto commentDto = json.parseObject(testData);
        assertThat(commentDto.equals(comment));
    }
}
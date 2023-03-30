package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonTest
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;
    private static final LocalDateTime START = LocalDateTime.of(2024, 10, 23, 17, 19, 33);
    private static final LocalDateTime END = LocalDateTime.of(2024, 10, 23, 17, 19, 45);
    private final UserDto user = new UserDto(1L, "user1", "y1@email.ru");
    private final Long requestId = 1L;
    private final List<CommentDto> comments = new ArrayList<>();

    @Test
    @SneakyThrows
    public void bookingDtoTest() {
        ItemDto item = new ItemDto(1L, "text", "desc", true, requestId, user, comments, null, null);
        String testData = String.format("{\"id\":1,\"name\":\"text\",\"description\":\"desc\",\"available\":true,\"requestId\"" +
                ":1,\"owner\":{\"id\":1,\"email\":\"user1\",\"name\":\"y1@email.ru\"},\"comments\":[],\"lastBooking\"" +
                ":null,\"nextBooking\":null}");
        ItemDto itemDto = json.parseObject(testData);
        AssertionsForClassTypes.assertThat(itemDto.equals(item)).isTrue();
    }
}

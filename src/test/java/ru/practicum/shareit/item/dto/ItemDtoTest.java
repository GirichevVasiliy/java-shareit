package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
        ItemDto itemDto = new ItemDto(1L, "text", "desc", true, requestId, user, comments, null, null);
        JsonContent<ItemDto> result = json.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo(null);
    }
}

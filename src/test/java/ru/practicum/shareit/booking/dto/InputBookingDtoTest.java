package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@JsonTest
public class InputBookingDtoTest {
    @Autowired
    private JacksonTester<InputBookingDto> json;
    private static final LocalDateTime START = LocalDateTime.of(2024, 10, 23, 17, 19, 33);
    private static final LocalDateTime END = LocalDateTime.of(2024, 10, 23, 17, 19, 45);
    private final UserDto user = new UserDto(1L, "user1", "y1@email.ru");
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item1")
            .description("text")
            .available(true)
            .owner(user)
            .requestId(1L)
            .build();

    @Test
    @SneakyThrows
    public void inputBookingDtoTest() {
        String testData = String.format("{\"itemId\":1,\"start\":\"2024-10-23T17:19:33\",\"end\":\"2024-10-23T17:19:45\"}");
        InputBookingDto inputBookingDto = json.parseObject(testData);
        AssertionsForClassTypes.assertThat(inputBookingDto.getItemId().equals(itemDto.getId())).isTrue();
        AssertionsForClassTypes.assertThat(inputBookingDto.getStart().equals(START)).isTrue();
        AssertionsForClassTypes.assertThat(inputBookingDto.getEnd().equals(END)).isTrue();
    }
}

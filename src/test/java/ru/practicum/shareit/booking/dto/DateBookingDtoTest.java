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
public class DateBookingDtoTest {
    @Autowired
    private JacksonTester<DateBookingDto> json;
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
    public void dateBookingDtoTest() {
        String testData = String.format("{\"id\":1,\"bookerId\":1,\"start\":\"2024-10-23T17:19:33\"," +
                "\"end\":\"2024-10-23T17:19:45\"}");
        DateBookingDto dateBookingDto = json.parseObject(testData);
        AssertionsForClassTypes.assertThat(dateBookingDto.getId().equals(1L)).isTrue();
        AssertionsForClassTypes.assertThat(dateBookingDto.getStart().equals(START)).isTrue();
        AssertionsForClassTypes.assertThat(dateBookingDto.getEnd().equals(END)).isTrue();
    }
}


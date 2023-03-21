package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
        InputBookingDto inputBookingDto = new InputBookingDto(itemDto.getId(), START, END);
        JsonContent<InputBookingDto> result = json.write(inputBookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotBlank();
    }
}

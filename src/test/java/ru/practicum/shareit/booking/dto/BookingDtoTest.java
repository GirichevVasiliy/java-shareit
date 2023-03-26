package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;
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
    public void bookingDtoTest() {
        String testData = String.format("{\"id\":1,\"start\":\"2024-10-23T17:19:33\",\"end\":\"2024-10-23T17:19:45\"," +
                "\"item\":{\"id\":1,\"name\":\"item1\",\"description\":\"text\",\"available\":true,\"requestId\":1," +
                "\"owner\":{\"id\":1,\"email\":\"user1\",\"name\":\"y1@email.ru\"},\"comments\":null,\"lastBooking\"" +
                ":null,\"nextBooking\":null},\"booker\":{\"id\":1,\"email\":\"user1\",\"name\":\"y1@email.ru\"},\"status\"" +
                ":\"APPROVED\"}");
        BookingDto bookingDto = json.parseObject(testData);
        assertThat(bookingDto.getId().equals(1L)).isTrue();
        assertThat(bookingDto.getStart().equals(START)).isTrue();
        assertThat(bookingDto.getEnd().equals(END)).isTrue();
        assertThat(bookingDto.getItem().equals(itemDto)).isTrue();
        assertThat(bookingDto.getBooker().equals(user)).isTrue();
        assertThat(bookingDto.getStatus().equals("APPROVED")).isTrue();
    }
}
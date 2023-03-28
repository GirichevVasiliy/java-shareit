package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    private BookingDto bookingDto;
    private UserDto ownerDto;
    private ItemDto itemDto;
    private UserDto userDto;
    Sort sortByStart = Sort.by(Sort.Direction.DESC, "start");
    Pageable pageable = PageRequest.of(0, 10, sortByStart);

    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(4L)
                .name("user4")
                .email("y@4email.ru")
                .build();
        ownerDto = UserDto.builder()
                .id(5L)
                .name("user5")
                .email("y@5email.ru")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .owner(ownerDto)
                .requestId(1L)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2023-05-12T17:00:00"))
                .end(LocalDateTime.parse("2023-05-12T17:05:00"))
                .item(itemDto)
                .booker(userDto)
                .status("APPROVED")
                .build();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetBookingById() {
        final Long bookingId = 1L;
        final Long ownerId = 5L;
        BookingDto newBooking = bookingService.getBookingById(bookingId, ownerId);
        assertThat(newBooking.equals(bookingDto)).isTrue();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetAllBookings() {
        final Long ownerId = 5L;
        final int size = 5;
        List<BookingDto> newBookings = bookingService.getAllBookingsForOwner(ownerId, StateBooking.ALL, pageable);
        assertThat(newBookings.size() == size).isTrue();
    }
}

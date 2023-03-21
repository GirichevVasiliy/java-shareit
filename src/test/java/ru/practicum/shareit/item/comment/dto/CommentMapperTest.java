package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.BeforeEach;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentMapperTest {
    private BookingDto bookingDtoControl;
    private InputBookingDto inputBookingDto;
    private Booking booking;
    private User owner;
    private ItemDto itemDto;
    private UserDto ownerDto;
    private ItemRequest itemRequest;
    private Item item;

    @BeforeEach
    private void init() {
        owner = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        ownerDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("text")
                .requestor(owner)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(ownerDto)
                .requestId(1L)
                .build();
        inputBookingDto = InputBookingDto.builder()
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(new Item(1L, "item1", "text", true, owner, itemRequest, new ArrayList<>()))
                .booker(owner)
                .status(StatusBooking.APPROVED)
                .build();
        bookingDtoControl = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(itemDto)
                .booker(ownerDto)
                .status(StatusBooking.APPROVED.name())
                .build();
        item = Item.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(owner)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();

    }

}

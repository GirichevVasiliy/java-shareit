package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class BookingMapperTest {
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

    @Test
    @DisplayName("Стандартный тест перевода booking в bookingDto")
    void bookingToDtoTest() {
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(bookingDtoControl.getId())).isTrue();
        assertThat(bookingDtoNew.getStart().equals(bookingDtoControl.getStart())).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(bookingDtoControl.getEnd())).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(bookingDtoControl.getBooker())).isTrue();
        assertThat(bookingDtoNew.getItem().equals(bookingDtoControl.getItem())).isTrue();
    }

    @Test
    @DisplayName("Tест проверки обновления id, при переводе booking в bookingDto")
    void bookingToDtoTestNewId() {
        final Long newId = 33L;
        booking.setId(newId);
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(newId)).isTrue();
        assertThat(bookingDtoNew.getStart().equals(bookingDtoControl.getStart())).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(bookingDtoControl.getEnd())).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(bookingDtoControl.getBooker())).isTrue();
        assertThat(bookingDtoNew.getItem().equals(bookingDtoControl.getItem())).isTrue();
    }

    @Test
    @DisplayName("Tест проверки обновления StartTime, при переводе booking в bookingDto")
    void bookingToDtoTestNewStartTime() {
        final LocalDateTime newTime = LocalDateTime.parse("2045-10-23T19:19:00");
        booking.setStart(LocalDateTime.parse("2045-10-23T19:19:00"));
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(bookingDtoControl.getId())).isTrue();
        assertThat(bookingDtoNew.getStart().equals(newTime)).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(bookingDtoControl.getEnd())).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(bookingDtoControl.getBooker())).isTrue();
        assertThat(bookingDtoNew.getItem().equals(bookingDtoControl.getItem())).isTrue();
    }

    @Test
    @DisplayName("Tест проверки обновления EndTime, при переводе booking в bookingDto")
    void bookingToDtoTestNewEndTime() {
        final LocalDateTime newTime = LocalDateTime.parse("2045-10-23T19:19:00");
        booking.setEnd(newTime);
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(bookingDtoControl.getId())).isTrue();
        assertThat(bookingDtoNew.getStart().equals(bookingDtoControl.getStart())).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(newTime)).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(bookingDtoControl.getBooker())).isTrue();
        assertThat(bookingDtoNew.getItem().equals(bookingDtoControl.getItem())).isTrue();
    }

    @Test
    @DisplayName("Tест проверки обновления User, при переводе booking в bookingDto")
    void bookingToDtoTestNewBooker() {
        User newUser = User.builder()
                .id(2L)
                .name("user555")
                .email("y555@email.ru")
                .build();
        UserDto newUserDto = UserDto.builder()
                .id(2L)
                .name("user555")
                .email("y555@email.ru")
                .build();
        booking.setBooker(newUser);
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(bookingDtoControl.getId())).isTrue();
        assertThat(bookingDtoNew.getStart().equals(bookingDtoControl.getStart())).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(bookingDtoControl.getEnd())).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(newUserDto)).isTrue();
        assertThat(bookingDtoNew.getItem().equals(bookingDtoControl.getItem())).isTrue();
    }

    @Test
    @DisplayName("Tест проверки обновления Item, при переводе booking в bookingDto")
    void bookingToDtoTestNewItem() {
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(new Item(2L, "item2", "text2", false, owner, itemRequest, new ArrayList<>()))
                .booker(owner)
                .status(StatusBooking.APPROVED)
                .build();
        itemDto = ItemDto.builder()
                .id(2L)
                .name("item2")
                .description("text2")
                .available(false)
                .owner(ownerDto)
                .requestId(1L)
                .build();
        BookingDto bookingDtoNew = BookingMapper.bookingToDto(booking);
        assertThat(bookingDtoNew.getId().equals(bookingDtoControl.getId())).isTrue();
        assertThat(bookingDtoNew.getStart().equals(bookingDtoControl.getStart())).isTrue();
        assertThat(bookingDtoNew.getEnd().equals(bookingDtoControl.getEnd())).isTrue();
        assertThat(bookingDtoNew.getBooker().equals(bookingDtoControl.getBooker())).isTrue();
        assertThat(bookingDtoNew.getItem().equals(itemDto)).isTrue();
    }

    @Test
    @DisplayName("Стандартный тест создани из bookingDto booking")
    void createNewBookingStandardTest() {
        Booking newBooking = BookingMapper.createNewBooking(inputBookingDto, owner, item);
        assertThat(newBooking.getStart().equals(booking.getStart())).isTrue();
        assertThat(newBooking.getEnd().equals(booking.getEnd())).isTrue();
        assertThat(newBooking.getBooker().equals(booking.getBooker())).isTrue();
        assertThat(newBooking.getItem().equals(booking.getItem())).isTrue();
        assertThat(newBooking.getStatus().equals(StatusBooking.WAITING)).isTrue();
    }

    @Test
    void toDateBookingDto() {
        DateBookingDto newDateBookingDto = BookingMapper.toDateBookingDto(booking);
        assertThat(newDateBookingDto.getId().equals(booking.getId()));
        assertThat(newDateBookingDto.getStart().equals(booking.getStart())).isTrue();
        assertThat(newDateBookingDto.getEnd().equals(booking.getEnd())).isTrue();
        assertThat(newDateBookingDto.getBookerId().equals(booking.getBooker().getId())).isTrue();
    }
}
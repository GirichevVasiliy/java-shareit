package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationAvailableException;
import ru.practicum.shareit.exception.ValidationDateException;
import ru.practicum.shareit.exception.ValidationOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private BookingDto bookingDto;
    private User user;
    private UserDto ownerDto;
    private InputBookingDto inputBookingDto;
    final Long userId1 = 1L;
    final Long userId2 = 2L;
    final Long bookingId = 1L;
    private ItemRequest itemRequest;
    private Item item;
    private User onwer;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());

    @BeforeEach
    private void init() {
        user = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        onwer = User.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();
        ownerDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();

        UserDto bookerDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .owner(ownerDto)
                .requestId(2L)
                .build();
        inputBookingDto = InputBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(itemDto)
                .booker(bookerDto)
                .status("APPROVED")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("text")
                .requestor(user)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        item = Item.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(onwer)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
    }

    @Test
    void addBooking_whenStartAndEndTimeIsOld_thenThrowException() {
        inputBookingDto.setStart(LocalDateTime.parse("2018-10-23T17:19:45"));
        inputBookingDto.setEnd(LocalDateTime.parse("2017-10-23T17:19:45"));
        assertThrows(
                ValidationDateException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenStartTimeIsOld_thenThrowException() {
        inputBookingDto.setStart(LocalDateTime.parse("2016-10-23T17:19:45"));
        inputBookingDto.setEnd(LocalDateTime.parse("2023-10-23T17:19:45"));
        assertThrows(
                ValidationDateException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenEndTimeIsBeforeStart_thenThrowException() {
        inputBookingDto.setStart(LocalDateTime.parse("2024-10-23T17:19:45"));
        inputBookingDto.setEnd(LocalDateTime.parse("2021-10-23T17:19:46"));
        assertThrows(
                ValidationDateException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenUserNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenItemNotFound_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenAvailableFalse_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(inputBookingDto.getItemId())).thenReturn(Optional.of(item));
        item.setAvailable(false);
        assertThrows(
                ValidationAvailableException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenUserIdNotEquelsOwner_thenThrowException() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(itemRepository.findById(inputBookingDto.getItemId())).thenReturn(Optional.of(item));
        item.setAvailable(true);
        assertThrows(
                ValidationOwnerException.class,
                () -> bookingService.addBooking(inputBookingDto, userId2));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenDataValid_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(inputBookingDto.getItemId())).thenReturn(Optional.of(item));
        item.setAvailable(true);
        when(bookingRepository.save(any())).thenReturn(any());
        bookingService.addBooking(inputBookingDto, userId1);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void updateApprove() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getAllBookings() {
    }

    @Test
    void getAllBookingsForOwner() {
    }
}
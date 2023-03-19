package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private Booking booking;
    private Item item;
    private ItemDto itemDto;
    private User onwer;
    private UserDto userDto;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());
    final int size = 0;
    private Page<Booking> page = new PageImpl<>(new ArrayList<>(), pageable, size);

    @BeforeEach
    private void init() {
        user = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        userDto = UserDto.builder()
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
        UserDto bookerDto2 = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
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
                .itemId(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(itemDto)
                .booker(userDto)
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
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(item)
                .booker(user)
                .status(StatusBooking.APPROVED)
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
    void forAllTests_whenUserNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.addBooking(inputBookingDto, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void forAllTests_whenItemNotFound_thenThrowException() {
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
    void forAllTests_whenUserIsOwner_thenThrowException() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(itemRepository.findById(inputBookingDto.getItemId())).thenReturn(Optional.of(item));
        item.setAvailable(true);
        assertThrows(
                ValidationOwnerException.class,
                () -> bookingService.addBooking(inputBookingDto, userId2));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenBookingСorrect_thenReturnBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(inputBookingDto.getItemId())).thenReturn(Optional.of(item));
        item.setAvailable(true);
        when(bookingRepository.save(any())).thenReturn(booking);
        bookingService.addBooking(inputBookingDto, userId1);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void forAllTests_whenBookingNotFound_thenThrowException() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.updateApprove(bookingId, true, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateApprove_whenUserIsNotOwnerItem_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(
                ValidationOwnerException.class,
                () -> bookingService.updateApprove(bookingId, true, userId1));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateApprove_whenTrue_thenReturnBookingDto() {
        booking.setStatus(StatusBooking.WAITING);
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto newBooking = bookingService.updateApprove(bookingId, true, userId2);
        assertThat(newBooking.getStatus().equals(StatusBooking.APPROVED.name())).isTrue();
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void updateApprove_whenFalse_thenReturnBookingDto() {
        booking.setStatus(StatusBooking.WAITING);
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto newBooking = bookingService.updateApprove(bookingId, false, userId2);
        assertThat(newBooking.getStatus().equals(StatusBooking.REJECTED.name())).isTrue();
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void updateApprove_whenStateIsNotWAITING_thenThrowException() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(
                ValidationStateException.class,
                () -> bookingService.updateApprove(bookingId, true, userId2));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBookingById_whenСorrect_thenReturnBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        BookingDto newBookingDto = bookingService.getBookingById(bookingId, userId1);
        assertThat(newBookingDto.equals(bookingDto)).isTrue();
        verify(bookingRepository, times(1)).findById(any());
    }

    @Test
    void getAllBookings_whenStateBookingALL_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBooker(user, pageable)).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.ALL, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findAllByBooker(any(), any());
    }

    @Test
    void getAllBookings_whenStateBookingCURRENT_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findCurrent(user, pageable)).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.CURRENT, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findCurrent(any(), any());
    }

    @Test
    void getAllBookings_whenStateBookingPAST_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndEndIsBefore(any(), any(), any())).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.PAST, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findAllByBookerAndEndIsBefore(any(), any(), any());
    }

    @Test
    void getAllBookings_whenStateBookingFUTURE_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStartIsAfter(any(), any(), any())).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.FUTURE, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findAllByBookerAndStartIsAfter(any(), any(), any());
    }

    @Test
    void getAllBookings_whenStateBookingWAITING_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStatus(any(), any(), any())).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.WAITING, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findAllByBookerAndStatus(any(), any(), any());
    }

    @Test
    void getAllBookings_whenStateBookingREJECTED_thenReturnListBookingDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStatus(any(), any(), any())).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings(userId1, StateBooking.REJECTED, pageable);
        assertThat(bookingDtoList.isEmpty());
        verify(bookingRepository).findAllByBookerAndStatus(any(), any(), any());
    }

    @Test
    void getAllBookingsForOwner_whenBookingALL_thenReturnListBookingDto() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findAllByOwner(userId2, pageable)).thenReturn(new ArrayList<>());
        List<BookingDto> list = bookingService.getAllBookingsForOwner(userId2, StateBooking.ALL, pageable);
        assertThat(list.isEmpty());
        verify(bookingRepository, times(1)).findAllByOwner(any(), any());
    }

    @Test
    void getAllBookingsForOwner_whenBookingREJECTED_thenReturnListBookingDto() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findAllByOwner(userId2, pageable)).thenReturn(new ArrayList<>());
        List<BookingDto> list = bookingService.getAllBookingsForOwner(userId2, StateBooking.REJECTED, pageable);
        assertThat(list.isEmpty());
        verify(bookingRepository, times(1)).findAllByOwner(any(), any());
    }

    @Test
    void getAllBookingsForOwner_whenBookingWAITING_thenReturnListBookingDto() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findAllByOwner(userId2, pageable)).thenReturn(new ArrayList<>());
        List<BookingDto> list = bookingService.getAllBookingsForOwner(userId2, StateBooking.WAITING, pageable);
        assertThat(list.isEmpty());
        verify(bookingRepository, times(1)).findAllByOwner(any(), any());
    }

    @Test
    void getAllBookingsForOwner_whenBookingFUTURE_thenReturnListBookingDto() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findAllByOwner(userId2, pageable)).thenReturn(new ArrayList<>());
        List<BookingDto> list = bookingService.getAllBookingsForOwner(userId2, StateBooking.FUTURE, pageable);
        assertThat(list.isEmpty());
        verify(bookingRepository, times(1)).findAllByOwner(any(), any());
    }

    @Test
    void getAllBookingsForOwner_whenBookingPAST_thenReturnListBookingDto() {
        when(userRepository.findById(userId2)).thenReturn(Optional.of(onwer));
        when(bookingRepository.findAllByOwner(userId2, pageable)).thenReturn(new ArrayList<>());
        List<BookingDto> list = bookingService.getAllBookingsForOwner(userId2, StateBooking.PAST, pageable);
        assertThat(list.isEmpty());
        verify(bookingRepository, times(1)).findAllByOwner(any(), any());
    }
}
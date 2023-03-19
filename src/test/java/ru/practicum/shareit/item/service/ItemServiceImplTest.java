package ru.practicum.shareit.item.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import ru.practicum.shareit.exception.ForbiddenResourceException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Captor
    private ArgumentCaptor<Item> argumentCaptor;
    private User user;
    private UserDto ownerDto;
    private InputBookingDto inputBookingDto;
    final Long userId1 = 1L;
    final Long userId2 = 2L;
    final Long itemId = 1L;
    final Long bookingId = 1L;
    final Long requestId = 1L;
    private ItemRequest itemRequest;
    private Booking booking;
    private Item item;
    private ItemDto itemDto;
    private User onwer;
    private UserDto userDto;
    private BookingDto bookingDto;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());
    final int size = 0;
    private Page<Item> pageItems = new PageImpl<>(new ArrayList<>(), pageable, size);
    private Page<Booking> pageBookings = new PageImpl<>(new ArrayList<>(), pageable, size);

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
    void addItem_whenСorrect_thenReturnItemDto() {
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto newItemDto = itemService.addItem(itemDto, userDto);
        verify(itemRepository, times(1)).save(any());
        assertThat(newItemDto.equals(itemDto)).isTrue();
    }
    @Test
    void addItem_whenRequestIdIsNull_thenReturnItemDto() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(ownerDto)
                .requestId(null)
                .build();
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto newItemDto = itemService.addItem(itemDto, userDto);
        verify(itemRepository, times(1)).save(any());
        assertThat(newItemDto.getRequestId().equals(item.getRequest().getRequestor().getId())).isTrue();
    }
    @Test
    void updateItem_whenUserIsNotOwnerItem_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        assertThrows(
                ForbiddenResourceException.class,
                () -> itemService.updateItem(itemId, itemDto, userId1));
        verify(bookingRepository, never()).save(any());
    }
    @Test
    void updateItem_whenNotFoundItem_thenThrowException() {
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.updateItem(itemId, itemDto, userId1));
        verify(bookingRepository, never()).save(any());
    }
    @Test
    void updateItem_whenRequestIdIsNull_thenReturnItemDto() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(ownerDto)
                .requestId(null)
                .build();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto newItemDto = itemService.updateItem(itemId, itemDto, userId2);
        verify(itemRepository, times(1)).save(any());
        assertThat(newItemDto.getRequestId().equals(item.getRequest().getRequestor().getId())).isTrue();
    }
    @Test
    void updateItem_whenNameUpdate_thenReturnItemDto() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto newItemDto = itemService.updateItem(itemId, itemDto, userId2);
        verify(itemRepository, times(1)).save(any());
        assertThat(newItemDto.equals(itemDto)).isTrue();
    }
    @Test
    void getItemById_whenUserNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.getItemById(itemId, userId1));
        verify(itemRepository, never()).save(any());
    }
    @Test
    void getItemById_whenItemNotFound_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.getItemById(itemId, userId1));
        verify(itemRepository, never()).save(any());
    }
    @Test
    void getItemById_whenСorrectData_thenReturnItemDto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(onwer));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ItemDto newItemDto = itemService.getItemById(itemId, userId2);
        verify(itemRepository, times(1)).findById(any());
        assertThat(newItemDto.equals(itemDto));
    }
    @Test
    void getItemsByUser_whenСorrectData_thenReturnListItemDto() {
        when(itemRepository.findByOwnerIdOrderById(any(), any())).thenReturn(pageItems);
        when(bookingRepository.findAllByItemIdInAndStatus(any(), any(), any())).thenReturn(pageBookings);
        List<ItemDto> newListItemDto = itemService.getItemsByUser(userId1, pageable);
        assertThat(newListItemDto.isEmpty());
        verify(itemRepository).findByOwnerIdOrderById(any(), any());
        verify(bookingRepository).findAllByItemIdInAndStatus(any(), any(), any());
    }
    @Test
    void getAvailableItems() {

    }

    @Test
    void deleteItemById() {
    }

    @Test
    void getAllItems() {
    }

    @Test
    void addComment() {
    }
}
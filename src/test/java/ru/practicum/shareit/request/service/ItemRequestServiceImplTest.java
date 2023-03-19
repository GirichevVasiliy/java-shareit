package ru.practicum.shareit.request.service;

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
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
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
    private CommentDto commentDto;
    private Comment comment;
    private ItemRequestDto itemRequestDto;

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
        commentDto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .authorName("Name")
                .created(LocalDateTime.parse("2023-10-23T17:19:45"))
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .author(user)
                .item(item)
                .created(LocalDateTime.parse("2023-10-23T17:19:45"))
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("text")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
    }
    @Test
    void forAllTests_whenUserNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.addItemRequest(itemRequestDto, userId1));
        verify(itemRequestRepository, never()).save(any());
    }
    @Test
    void addItemRequest_whenСorrectData_thenReturnItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);
        verify(itemRequestRepository, times(1)).save(any());
        assertThat(newItemRequest.equals(itemRequest)).isTrue();
    }
    @Test
    void getAllItemRequestsUser() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId1)).thenReturn(new ArrayList<ItemRequest>());
        when( itemRepository.findAllByRequestIn(Arrays.asList(itemRequest))).thenReturn(new ArrayList<Item>());
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllItemRequestsUser(userId1);
        assertThat(requestDtoList.isEmpty()).isTrue();
    }
    @Test
    void getAllItemRequests() {
    }

    @Test
    void getItemRequestById() {
    }
}
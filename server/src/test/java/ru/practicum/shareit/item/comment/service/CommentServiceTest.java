package ru.practicum.shareit.item.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.ValidationDateBookingException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
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
class CommentServiceTest {
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
    private User user;
    final Long userId1 = 1L;
    final Long itemId = 1L;
    final Long bookingId = 1L;
    private ItemRequest itemRequest;
    private Booking booking;
    private Item item;

    private User onwer;
    private CommentDto commentDto;
    private Comment comment;

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
    }

    @Test
    void addComment_whenUserNotBookingItem_thenThrowException() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(
                InvalidOwnerException.class,
                () -> itemService.addComment(itemId, userId1, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void addComment_whenBookingNotCompleted_thenThrowException() {
        final int limit = 1;
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.bookingСonfirmation(bookingId, itemId, limit)).thenReturn(Optional.of(booking));
        assertThrows(
                ValidationDateBookingException.class,
                () -> itemService.addComment(itemId, userId1, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void addComment_whenСorrectData_thenReturnCommentDto() {
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.parse("2021-10-23T17:19:33"))
                .end(LocalDateTime.parse("2021-10-23T17:19:45"))
                .item(item)
                .booker(user)
                .status(StatusBooking.REJECTED)
                .build();
        final int limit = 1;
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.bookingСonfirmation(bookingId, itemId, limit)).thenReturn(Optional.of(booking));
        when(bookingRepository.findByBookerAndItem(any(), any(), any(), any(), any())).thenReturn(Optional.ofNullable(booking));
        when(commentRepository.save(any())).thenReturn(comment);
        CommentDto newCommentDto = itemService.addComment(itemId, userId1, commentDto);
        verify(commentRepository, times(1)).save(any());
    }
}

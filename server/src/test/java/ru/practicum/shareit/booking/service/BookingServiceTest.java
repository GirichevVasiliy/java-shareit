package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationAvailableException;
import ru.practicum.shareit.exception.ValidationDateException;
import ru.practicum.shareit.exception.ValidationOwnerException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.util.PageableFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    ItemServiceImpl itemService;
    @Autowired
    ItemRequestServiceImpl itemRequestService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BookingRepository bookingRepository;
    private UserDto ownerDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private UserDto userDto2;
    private CommentDto commentDto;
    private CommentDto commentDtoSecond;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y@1email.ru")
                .build();
        ownerDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y@2email.ru")
                .build();
        userDto2 = UserDto.builder()
                .id(3L)
                .name("user3")
                .email("y@3email.ru")
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("comment1")
                .authorName("user2")
                .created(LocalDateTime.parse("2023-05-12T18:00"))
                .build();
        commentDtoSecond = CommentDto.builder()
                .text("comment2")
                .authorName("user1")
                .created(LocalDateTime.parse("2023-05-12T10:00"))
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .requestId(1L)
                .owner(ownerDto)
                .comments(Arrays.asList(commentDto))
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .description("req1")
                .build();
    }

    @Test
    public void bookingIntegrationTest() {
        UserDto saveUserDto = userService.addUser(userDto);
        UserDto saveOwnerDto = userService.addUser(ownerDto);
        UserDto saveUserDto2 = userService.addUser(userDto2);
        List<UserDto> allUsers = userService.getAllUsers();
        assertThat(allUsers.contains(saveUserDto)).isTrue();
        assertThat(allUsers.contains(saveOwnerDto)).isTrue();
        assertThat(allUsers.contains(saveUserDto2)).isTrue();

        ItemDto itemDtoForSave = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .build();
        ItemDto saveItemDto = itemService.addItem(itemDtoForSave, saveOwnerDto);

        InputBookingDto inputBookingDto1 = InputBookingDto.builder()
                .itemId(saveItemDto.getId())
                .start(LocalDateTime.parse("2023-04-01T13:00"))
                .end(LocalDateTime.parse("2024-04-29T10:00"))
                .build();

        BookingDto saveBookingDto1 = bookingService.addBooking(inputBookingDto1, userDto.getId());
        assertThat(saveBookingDto1.getStart().equals(inputBookingDto1.getStart())).isTrue();
        assertThat(saveBookingDto1.getEnd().equals(inputBookingDto1.getEnd())).isTrue();
        assertThat(saveBookingDto1.getId().equals(1L)).isTrue();
        assertThat(saveBookingDto1.getItem().equals(saveItemDto)).isTrue();
        assertThat(saveBookingDto1.getStatus().equals(StateBooking.WAITING.name())).isTrue();
        assertThat(saveBookingDto1.getBooker().equals(userDto)).isTrue();

        InputBookingDto inputBookingDto2 = InputBookingDto.builder()
                .itemId(saveItemDto.getId())
                .start(LocalDateTime.parse("2000-04-01T13:00"))
                .end(LocalDateTime.parse("2000-04-29T10:00"))
                .build();
        assertThrows(ValidationDateException.class, () -> bookingService.addBooking(inputBookingDto2, userDto2.getId()));
        final Long badIdUser = 99L;
        assertThrows(ResourceNotFoundException.class, () -> bookingService.addBooking(inputBookingDto1, badIdUser));

        InputBookingDto inputBookingDtoBadItem = InputBookingDto.builder()
                .itemId(99L)
                .start(LocalDateTime.parse("2023-04-01T13:00"))
                .end(LocalDateTime.parse("2024-04-29T10:00"))
                .build();
        assertThrows(ResourceNotFoundException.class, () -> bookingService.addBooking(inputBookingDtoBadItem, userDto2.getId()));

        BookingDto updateApproveBooking1 = bookingService.updateApprove(saveBookingDto1.getId(), true, ownerDto.getId());
        assertThat(updateApproveBooking1.getStatus().equals(StatusBooking.APPROVED.name()));

        BookingDto bookingByIdOwnerItem = bookingService.getBookingById(saveBookingDto1.getId(), ownerDto.getId());
        assertThat(bookingByIdOwnerItem.equals(updateApproveBooking1)).isTrue();

        BookingDto bookingByIdOwnerBooking = bookingService.getBookingById(saveBookingDto1.getId(), userDto.getId());
        assertThat(bookingByIdOwnerBooking.equals(updateApproveBooking1)).isTrue();

        assertThrows(ValidationOwnerException.class, () -> bookingService.getBookingById(saveBookingDto1.getId(), userDto2.getId()));

        InputBookingDto inputBookingDto3 = InputBookingDto.builder()
                .itemId(saveItemDto.getId())
                .start(LocalDateTime.parse("2025-04-01T13:00"))
                .end(LocalDateTime.parse("2026-04-29T10:00"))
                .build();
        BookingDto saveBookingDto3 = bookingService.addBooking(inputBookingDto3, userDto.getId());
        assertThat(saveBookingDto3.getStart().equals(inputBookingDto3.getStart())).isTrue();
        assertThat(saveBookingDto3.getEnd().equals(inputBookingDto3.getEnd())).isTrue();
        assertThat(saveBookingDto3.getId().equals(2L)).isTrue();
        assertThat(saveBookingDto3.getItem().equals(saveItemDto)).isTrue();
        assertThat(saveBookingDto3.getStatus().equals(StateBooking.WAITING.name())).isTrue();
        assertThat(saveBookingDto3.getBooker().equals(userDto)).isTrue();

        final int size2 = 2;
        List<BookingDto> allBookings = bookingService.getAllBookings(userDto.getId(), StateBooking.ALL,
                PageableFactory.getPageableSortDescStart(0, 10));
        assertThat(allBookings.size() == size2).isTrue();
        List<BookingDto> allBookingsOwner = bookingService.getAllBookingsForOwner(ownerDto.getId(), StateBooking.WAITING,
                PageableFactory.getPageableSortDescStart(0, 10));
        final int size1 = 1;
        assertThat(allBookingsOwner.size() == size1).isTrue();

        ItemDto itemDtoNotAvailable = ItemDto.builder()
                .id(4L)
                .name("item4")
                .description("item4Desc")
                .available(false)
                .build();
        ItemDto saveItemDtoNotAvailable = itemService.addItem(itemDtoNotAvailable, saveOwnerDto);
        InputBookingDto inputBookingDtoBadAvailable = InputBookingDto.builder()
                .itemId(saveItemDtoNotAvailable.getId())
                .start(LocalDateTime.parse("2023-04-01T13:00"))
                .end(LocalDateTime.parse("2024-04-29T10:00"))
                .build();
        assertThrows(ValidationAvailableException.class, () -> bookingService.addBooking(inputBookingDtoBadAvailable, userDto.getId()));

        ItemDto itemDtoOwner = ItemDto.builder()
                .id(4L)
                .name("item4")
                .description("item4Desc")
                .available(true)
                .build();
        ItemDto saveItemDtoOwner = itemService.addItem(itemDtoOwner, saveOwnerDto);
        InputBookingDto inputBookingDtoOwner = InputBookingDto.builder()
                .itemId(saveItemDtoOwner.getId())
                .start(LocalDateTime.parse("2023-04-01T13:00"))
                .end(LocalDateTime.parse("2024-04-29T10:00"))
                .build();
        assertThrows(ValidationOwnerException.class, () -> bookingService.addBooking(inputBookingDtoOwner, ownerDto.getId()));
    }
}

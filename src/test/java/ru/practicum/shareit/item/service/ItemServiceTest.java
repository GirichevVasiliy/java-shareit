package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.util.CreatePageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    @Autowired
    ItemServiceImpl itemService;
    @Autowired
    ItemRequestServiceImpl itemRequestService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;
    private UserDto ownerDto;
    private ItemDto itemDto;
    private UserDto userDto;
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
                .email("y@5email.ru")
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("comment1")
                .authorName("user2")
                .created(LocalDateTime.parse("2023-05-12T18:00"))
                .build();
        commentDtoSecond = CommentDto.builder()
                .text("comment2")
                .authorName("user2")
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
    public void itemIntegrationTest() {
        UserDto saveUserDto = userService.addUser(userDto);
        UserDto saveOwnerDto = userService.addUser(ownerDto);

        List<UserDto> all = userService.getAllUsers();
        ItemRequestDto saveItemRequestDto = itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto),
                saveUserDto.getId());

        ItemDto itemDtoForSave = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .requestId(1L)
                .build();
        ItemDto saveItemDto = itemService.addItem(itemDtoForSave, saveOwnerDto);
        assertThat(saveItemDto.getId().equals(1L)).isTrue();
        assertThat(saveItemDto.getName().equals(itemDto.getName())).isTrue();
        assertThat(saveItemDto.getDescription().equals(itemDto.getDescription())).isTrue();
        assertThat(saveItemDto.getAvailable().equals(itemDto.getAvailable())).isTrue();


        ItemDto itemDtoForUpdate = ItemDto.builder()
                .id(1L)
                .name("itemNew")
                .description("itemNewDesc")
                .available(false)
                .requestId(1L)
                .build();
        ItemDto updateItemDto = itemService.updateItem(1L, itemDtoForUpdate, saveOwnerDto.getId());
        assertThat(updateItemDto.getId().equals(1L)).isTrue();
        assertThat(updateItemDto.getName().equals(itemDtoForUpdate.getName())).isTrue();
        assertThat(updateItemDto.getDescription().equals(itemDtoForUpdate.getDescription())).isTrue();
        assertThat(updateItemDto.getAvailable().equals(itemDtoForUpdate.getAvailable())).isTrue();

        ItemDto itemDto2 = ItemDto.builder()
                .id(2L)
                .name("item2")
                .description("item2Desc")
                .available(true)
                .requestId(1L)
                .build();
        ItemDto itemDtoForBooking = itemService.addItem(itemDto2, saveOwnerDto);
        ItemDto itemDtoById = itemService.getItemById(2L, saveUserDto.getId());
        assertThat(itemDtoById.getId().equals(2L)).isTrue();
        assertThat(itemDtoById.getName().equals(itemDto2.getName())).isTrue();
        assertThat(itemDtoById.getDescription().equals(itemDto2.getDescription())).isTrue();
        assertThat(itemDtoById.getAvailable().equals(itemDto2.getAvailable())).isTrue();

        bookingRepository.save(new Booking(1L, LocalDateTime.parse("2022-03-29T10:00"),
                LocalDateTime.parse("2022-04-29T10:00"), ItemMapper.toItem(itemDtoForBooking),
                        UserMapper.dtoToUser(saveUserDto), StatusBooking.APPROVED));

        CommentDto commentDto1 = CommentDto.builder()
                .text("comment1")
                .authorName("user2")
                .created(LocalDateTime.now())
                .build();
        CommentDto saveCommentDto = itemService.addComment(itemDtoForBooking.getId(), saveUserDto.getId(), commentDto1);
        ItemDto itemContainsComment = itemService.getItemById(itemDtoForBooking.getId(), ownerDto.getId());
        assertThat(itemContainsComment.getComments().contains(saveCommentDto)).isTrue();

        List<ItemDto> itemDtoList = itemService.getItemsByUser(ownerDto.getId(), CreatePageable.getPageable(0, 10));
    }
}
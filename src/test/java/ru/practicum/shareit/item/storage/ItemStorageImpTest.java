package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemStorageImpTest {
    @Autowired
    ItemStorage itemStorage;
    private User user;
    private UserDto ownerDto;
    final Long userId1 = 1L;
    final Long userId2 = 2L;
    final Long itemId = 1L;
    final Long bookingId = 1L;
    final Long requestId = 1L;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;
    private Booking booking;
    private Item item;
    private ItemDto itemDto;
    private User onwer;
    private UserDto userDto;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());
    final int size = 0;
    private Page<Item> pageItems = new PageImpl<>(new ArrayList<>(), pageable, size);
    private Page<Booking> pageBookings = new PageImpl<>(new ArrayList<>(), pageable, size);
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    private void init() {
        itemStorage = new ItemStorageImp();
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
        itemRequest2 = ItemRequest.builder()
                .id(2L)
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
        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .author(user)
                .item(item)
                .created(LocalDateTime.parse("2023-10-23T17:19:45"))
                .build();
    }

    @Test
    void addItemValidItemAndUser() {
        Item newItem = itemStorage.addItem(item, onwer);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void addItemNotValidItem() {
        assertThrows(
                NullPointerException.class,
                () -> itemStorage.addItem(null, onwer));
    }

    @Test
    void addItemNotValidUser() {
        Item newItem = itemStorage.addItem(item, null);
        assertThat(newItem.getId().equals(item.getId())).isTrue();
        assertThat(newItem.getName().equals(item.getName())).isTrue();
        assertThat(newItem.getAvailable().equals(item.getAvailable())).isTrue();
        assertThat(newItem.getOwner() == null).isTrue();
        assertThat(newItem.getRequest().equals(item.getRequest())).isTrue();
        assertThat(newItem.getDescription().equals(item.getDescription())).isTrue();
        assertThat(newItem.getComments().equals(item.getComments())).isTrue();
    }

    @Test
    void updateItemUpdateName() {
        itemStorage.addItem(item, onwer);
        item = Item.builder()
                .id(1L)
                .name("itemNew")
                .description("text")
                .available(true)
                .owner(onwer)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        Item newItem = itemStorage.updateItem(1L, item, 2L);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void updateItemUpdateDescription() {
        itemStorage.addItem(item, onwer);
        item = Item.builder()
                .id(1L)
                .name("itemNew")
                .description("NewText")
                .available(true)
                .owner(onwer)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        Item newItem = itemStorage.updateItem(1L, item, 2L);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void updateItemUpdateAvailable() {
        itemStorage.addItem(item, onwer);
        item = Item.builder()
                .id(1L)
                .name("itemNew")
                .description("NewText")
                .available(false)
                .owner(onwer)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        Item newItem = itemStorage.updateItem(1L, item, 2L);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void updateItemUpdateOwner() {
        itemStorage.addItem(item, onwer);
        item = Item.builder()
                .id(1L)
                .name("itemNew")
                .description("NewText")
                .available(false)
                .owner(user)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        Item newItem = itemStorage.updateItem(1L, item, 1L);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void updateItemUpdateItemRequest() {
        itemStorage.addItem(item, onwer);
        item = Item.builder()
                .id(1L)
                .name("itemNew")
                .description("NewText")
                .available(false)
                .owner(user)
                .comments(new ArrayList<>())
                .request(itemRequest2)
                .build();
        Item newItem = itemStorage.updateItem(1L, item, 1L);
        assertThat(newItem.equals(item)).isTrue();
    }

    @Test
    void getItemByIdTest() {
        itemStorage.addItem(item, onwer);
        Optional<Item> newItem = itemStorage.getItemById(itemId, userId2);
        assertThat(newItem.get().equals(item)).isTrue();
    }

    @Test
    void getItemByIdItemIdBadTest() {
        Optional<Item> newItem = itemStorage.getItemById(itemId, userId2);
        assertThat(newItem.isEmpty()).isTrue();
    }

    @Test
    void getItemsByUserListEmpty() {
        List<Item> items = itemStorage.getItemsByUser(userId1);
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    void getItemsByUserListItems() {
        itemStorage.addItem(item, onwer);
        List<Item> items = itemStorage.getItemsByUser(userId2);
        assertThat(items.contains(item)).isTrue();
    }

    @Test
    void getAvailableItems() {
        itemStorage.addItem(item, onwer);
        List<Item> items = itemStorage.getAvailableItems(userId2, "text");
        assertThat(items.contains(item)).isTrue();
    }

    @Test
    void deleteItemById() {
        itemStorage.addItem(item, onwer);
    }

    @Test
    void getAllItems() {
        itemStorage.addItem(item, onwer);
    }
}
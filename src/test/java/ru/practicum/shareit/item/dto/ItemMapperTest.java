package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemMapperTest {
    private User owner;
    private ItemDto itemDto;
    private UserDto ownerDto;
    private ItemRequest itemRequest;
    private DateBookingDto last;
    private DateBookingDto next;
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
        item = Item.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(owner)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        next = DateBookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-25T17:19:30"))
                .end(LocalDateTime.parse("2024-10-25T17:19:33"))
                .build();
        last = DateBookingDto.builder()
                .id(2L)
                .start(LocalDateTime.parse("2022-10-25T17:19:30"))
                .end(LocalDateTime.parse("2022-10-25T17:19:33"))
                .build();
    }

    @Test
    @DisplayName("Тест перевода Item в ItemDto")
    void toItemDtoSinglTest() {
        ItemDto newItemDto = ItemMapper.toItemDtoSingl(item);
        assertThat(newItemDto.getId().equals(item.getId())).isTrue();
        assertThat(newItemDto.getAvailable().equals(item.getAvailable())).isTrue();
        assertThat(newItemDto.getRequestId().equals(item.getRequest().getId())).isTrue();
        assertThat(newItemDto.getName().equals(item.getName())).isTrue();
        assertThat(newItemDto.getDescription().equals(item.getDescription())).isTrue();
        assertThat(newItemDto.getOwner().getId().equals(item.getOwner().getId())).isTrue();
        assertThat(newItemDto.getOwner().getName().equals(item.getOwner().getName())).isTrue();
        assertThat(newItemDto.getOwner().getEmail().equals(item.getOwner().getEmail())).isTrue();
    }

    @Test
    @DisplayName("Тест перевода ItemDto в Item")
    void toItemTest() {
        Item newItem = ItemMapper.toItem(itemDto);
        assertThat(newItem.getId().equals(itemDto.getId())).isTrue();
        assertThat(newItem.getName().equals(itemDto.getName())).isTrue();
        assertThat(newItem.getDescription().equals(itemDto.getDescription())).isTrue();
        assertThat(newItem.getAvailable().equals(itemDto.getAvailable())).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Item в ItemDtoFull")
    void toItemDtoFull() {
        ItemDto itemDtoFull = ItemMapper.toItemDtoFull(item, new ArrayList<>(), last, next);
        assertThat(itemDtoFull.getId().equals(item.getId())).isTrue();
        assertThat(itemDtoFull.getName().equals(item.getName())).isTrue();
        assertThat(itemDtoFull.getAvailable().equals(item.getAvailable())).isTrue();
        assertThat(itemDtoFull.getRequestId().equals(item.getRequest().getId())).isTrue();
        assertThat(itemDtoFull.getOwner().equals(ownerDto)).isTrue();
        assertThat(itemDtoFull.getComments().equals(item.getComments())).isTrue();
        assertThat(itemDtoFull.getDescription().equals(item.getDescription())).isTrue();
        assertThat(itemDtoFull.getLastBooking().equals(last)).isTrue();
        assertThat(itemDtoFull.getNextBooking().equals(next)).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Item и списка комментариев в ItemDto")
    void toItemDtoListTest() {
        List<CommentDto> commentDtoList = Arrays.asList(new CommentDto(1L, "text", "name",
                LocalDateTime.parse("2024-10-23T17:19:33")));
        ItemDto newItemDto = ItemMapper.toItemDtoList(item, commentDtoList);
        assertThat(newItemDto.getId().equals(item.getId())).isTrue();
        assertThat(newItemDto.getName().equals(item.getName())).isTrue();
        assertThat(newItemDto.getAvailable().equals(item.getAvailable())).isTrue();
        assertThat(newItemDto.getRequestId().equals(item.getRequest().getId())).isTrue();
        assertThat(newItemDto.getDescription().equals(item.getDescription())).isTrue();
        assertThat(newItemDto.getOwner().equals(ownerDto)).isTrue();
        assertThat(newItemDto.getComments().equals(commentDtoList)).isTrue();
        assertThat(newItemDto.getLastBooking() == null).isTrue();
        assertThat(newItemDto.getNextBooking() == null).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Item в Answer")
    void answerCreateForItem() {
        Answer answer = ItemMapper.answerCreateForItem(item);
        assertThat(answer.getItemId().equals(item.getId())).isTrue();
        assertThat(answer.getItemName().equals(item.getName())).isTrue();
        assertThat(answer.getAvailable().equals(item.getAvailable())).isTrue();
        assertThat(answer.getRequestId().equals(item.getRequest().getId())).isTrue();
        assertThat(answer.getDescription().equals(item.getDescription())).isTrue();
    }
}
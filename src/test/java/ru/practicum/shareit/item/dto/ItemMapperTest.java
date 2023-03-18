package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.data.util.Predicates.isTrue;

class ItemMapperTest {
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
    @DisplayName("Стандартный тест перевода Item в ItemDto")
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
    @DisplayName("Стандартный тест перевода ItemDto в Item")
    void toItemTest() {
        Item newItem = ItemMapper.toItem(itemDto);
        assertThat(newItem.getId().equals(itemDto.getId())).isTrue();
        assertThat(newItem.getName().equals(itemDto.getName())).isTrue();
        assertThat(newItem.getDescription().equals(itemDto.getDescription())).isTrue();
        assertThat(newItem.getAvailable().equals(itemDto.getAvailable())).isTrue();
    }

    @Test
    void toItemDtoFull() {
    }

    @Test
    void toItemDtoList() {
    }

    @Test
    void answerCreateForItem() {
    }
}
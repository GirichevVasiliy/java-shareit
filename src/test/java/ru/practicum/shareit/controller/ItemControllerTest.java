package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemory;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryInMemory;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("Тест вещей")
class ItemControllerTest {
    private ItemController itemController;
   private UserService userService;
    private ItemDto item1;
    private ItemDto item2;
    private ItemDto item3;
    private ItemDto item4;
    private ItemDto item5;
    @BeforeEach
    private void init() {
        userService = new UserServiceImpl(new UserRepositoryInMemory());
        itemController = new ItemController(new ItemServiceImpl(new ItemRepositoryInMemory()),userService);
    }
    @BeforeEach
    private void initItemDto() {
       item1 = new ItemDto("Дрель", "Дрель перфоратор + набор сверл", true, null);
       item2 = new ItemDto("Шуруповерт", "Шуруповерт + аккумулятор", true, null);
       item3 = new ItemDto("Шурик", "Шуруповерт аккумуляторный", true, null);
       item4 = new ItemDto("Шлифовальная машина", "Шлифовальная машина аккумуляторный", true, null);
       item5 = new ItemDto("Штроборез", "Штроборез аккумуляторный", false, null);
    }
    @BeforeEach
    private void initUserDto() {
       UserDto userDto1 = new UserDto(1L, "user1@email.mail", "user1-name");
        UserDto userDto2 = new UserDto(2L, "user2@email.mail", "user2-name");
        UserDto  userDto3 = new UserDto(3L, "user3@email.mail", "user3-name");
        userService.addUser(userDto1);
        userService.addUser(userDto2);
        userService.addUser(userDto3);

    }

    @Test
    @DisplayName("Тест добавления вещи")
    void addItem() {
        final Long id = 1L;
        ItemDto newItemDto = itemController.addItem(id, item1);
        item1.setId(id);
        assertThat(item1).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления имени вещи")
    void updateItemName() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setName("Новая дрель");
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        item1.setId(id);
        assertThat(item1).isEqualTo(newItemDto);
    }
    @Test
    @DisplayName("Тест обновления описания вещи")
    void updateItemDescription() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setDescription("Сломана вилка");
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        item1.setId(id);
        assertThat(item1).isEqualTo(newItemDto);
    }
    @Test
    @DisplayName("Тест обновления статуса вещи")
    void updateItemAvailable() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setAvailable(false);
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        assertThat(newItemDto.getAvailable()).isFalse();
    }
    @Test
    @DisplayName("Тест поиска вещи по ID")
    void getItemById() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setId(id);
        assertThat(itemController.getItemById(id, id)).isEqualTo(item1);

    }

    @Test
    void getItemsByUser() {
    }

    @Test
    void getAvailableItems() {
    }
}
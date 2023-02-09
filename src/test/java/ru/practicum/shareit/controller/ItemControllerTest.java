package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.error.RequestError;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemory;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryInMemory;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        itemController = new ItemController(new ItemServiceImpl(new ItemRepositoryInMemory()), userService);
    }

    @BeforeEach
    private void initItemDto() {
        item1 = new ItemDto(1L,"Дрель", "Дрель перфоратор + набор сверл", true, null);
        item2 = new ItemDto(2L,"Шуруповерт", "Шуруповерт + аккумулятор", true, null);
        item3 = new ItemDto(3L,"Шурик", "Шуруповерт аккумуляторный", true, null);
        item4 = new ItemDto(4L,"Шлифовальная машина", "Шлифовальная машина аккумуляторный", true, null);
        item5 = new ItemDto(5L,"Штроборез", "Штроборез аккумуляторный", false, null);
    }

    @BeforeEach
    private void initUserDto() {
        UserDto userDto1 = new UserDto(1L, "user1@email.mail", "user1-name");
        UserDto userDto2 = new UserDto(2L, "user2@email.mail", "user2-name");
        UserDto userDto3 = new UserDto(3L, "user3@email.mail", "user3-name");
        userService.addUser(userDto1);
        userService.addUser(userDto2);
        userService.addUser(userDto3);
    }

    private void addItems() {
        final Long idUser2 = 2L;
        itemController.addItem(idUser2, item1);
        final Long idUser1 = 1L;
        itemController.addItem(idUser1, item2);
        itemController.addItem(idUser1, item3);
        itemController.addItem(idUser1, item4);
        itemController.addItem(idUser1, item5);

    }

    @Test
    @DisplayName("Тест добавления вещи")
    void addItemTest() {
        final Long id = 1L;
        ItemDto newItemDto = itemController.addItem(id, item1);
        assertThat(item1).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления имени вещи")
    void updateItemNameTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setName("Новая дрель");
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        assertThat(item1).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления описания вещи")
    void updateItemDescriptionTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setDescription("Сломана вилка");
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        assertThat(item1).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления статуса вещи")
    void updateItemAvailableTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        item1.setAvailable(false);
        ItemDto newItemDto = itemController.updateItem(id, item1, id);
        assertThat(newItemDto.getAvailable()).isFalse();
    }

    @Test
    @DisplayName("Тест поиска вещи по ID")
    void getItemByIdTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        assertThat(itemController.getItemById(id, id)).isEqualTo(item1);
    }

    @Test
    @DisplayName("Тест поиска вещи по неверному ID")
    void getItemByBadIdTest() {
        final Long idItem = 999L;
        final Long userID = 1L;
        assertThrows(RequestError.class, () -> itemController.getItemById(userID, idItem));
        try {
            itemController.getItemById(userID, idItem);
        } catch (RequestError e) {
            assertThat(e.getStatus().is4xxClientError()).isTrue();
        }
    }

    @Test
    @DisplayName("Тест поиска всех вещей пользователя")
    void getItemsByUserTest() {
        addItems();
        final Long userID = 1L;
        final int size = 4;
        assertThat(itemController.getItemsByUser(userID).size() == size).isTrue();
        assertThat(itemController.getItemsByUser(userID).contains(item2)).isTrue();
        assertThat(itemController.getItemsByUser(userID).contains(item3)).isTrue();
        assertThat(itemController.getItemsByUser(userID).contains(item4)).isTrue();
        assertThat(itemController.getItemsByUser(userID).contains(item5)).isTrue();
    }

    @Test
    @DisplayName("Тест поиска всех вещей неизвестного пользователя пользователя")
    void getItemsByUserBadIDTest() {
        addItems();
        final Long userID = 999L;
        assertThat(itemController.getItemsByUser(userID).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест поиска вещей по названию")
    void getAvailableItemsTest() {
        addItems();
        final Long userID = 1L;
        final int size = 3;
        final String text = "аКкУмуЛЯтоР";
        assertThat(itemController.getAvailableItems(userID, text).size() == size).isTrue();
    }

    @Test
    @DisplayName("Тест поиска вещей по названию - нет такой вещи")
    void getAvailableItemsIsEmptyTest() {
        addItems();
        final Long userID = 1L;
        final String text = "станок";
        assertThat(itemController.getAvailableItems(userID, text).isEmpty()).isTrue();
    }
}
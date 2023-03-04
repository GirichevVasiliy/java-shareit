/*

package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {
    private ItemController itemController;
    private UserService userService;
    private ItemDto item1;
    private ItemDto item2;
    private ItemDto item3;
    private ItemDto item4;
    private ItemDto item5;

    @Sql(value = {"classpath:dataSQLTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @BeforeEach
    private void initItemDto() {
        item1 = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Дрель перфоратор + набор сверл")
                .available(true)
                .build();
        item2 = ItemDto.builder()
                .id(2L)
                .name("Шуруповерт")
                .description("Шуруповерт + аккумулятор")
                .available(true)
                .build();
        item3 = ItemDto.builder()
                .id(3L)
                .name("Шурик")
                .description("Шуруповерт аккумуляторный")
                .available(true)
                .build();
        item4 = ItemDto.builder()
                .id(4L)
                .name("Шлифовальная машина")
                .description("Шлифовальная машина аккумуляторный")
                .available(true)
                .build();
        item5 = ItemDto.builder()
                .id(5L)
                .name("Штроборез")
                .description("Штроборез аккумуляторный")
                .available(false)
                .build();
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
        ItemDto itemNew = ItemDto.builder()
                .id(1L)
                .name("Новая дрель")
                .description("Дрель перфоратор + набор сверл")
                .available(true)
                .build();
        ItemDto newItemDto = itemController.updateItem(id, itemNew, id);
        assertThat(itemNew).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления описания вещи")
    void updateItemDescriptionTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        ItemDto itemNew = ItemDto.builder()
                .id(1L)
                .name("Новая дрель")
                .description("Сломана вилка")
                .available(true)
                .build();
        ItemDto newItemDto = itemController.updateItem(id, itemNew, id);
        assertThat(itemNew).isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Тест обновления статуса вещи")
    void updateItemAvailableTest() {
        final Long id = 1L;
        itemController.addItem(id, item1);
        ItemDto itemNew = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Дрель перфоратор + набор сверл")
                .available(false)
                .build();
        ItemDto newItemDto = itemController.updateItem(id, itemNew, id);
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
        assertThrows(ResourceNotFoundException.class, () -> itemController.getItemById(userID, idItem));
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
*/

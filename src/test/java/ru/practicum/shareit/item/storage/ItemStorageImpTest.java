package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;

@SpringBootTest
class ItemStorageImpTest {
    @Autowired
    ItemStorage itemStorage;
    @BeforeEach
    private void init(){

    }

    @Test
    void addItem() {

    }

    @Test
    void updateItem() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void getItemsByUser() {
    }

    @Test
    void getAvailableItems() {
    }

    @Test
    void deleteItemById() {
    }

    @Test
    void getAllItems() {
    }
}
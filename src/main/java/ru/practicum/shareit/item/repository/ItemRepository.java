package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long itemId, Long userId);

    List<Item> getItemsByUser(Long userId);
    List<Item> getAvailableItems(Long userId, String text);
    void deleteItemById(Long itemId, Long userId);
    List<Item> getAllItems();
}

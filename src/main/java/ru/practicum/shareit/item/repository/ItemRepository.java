package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Item item, User user);

    Item updateItem(Long itemId, Item item, Long userId);

    Optional<Item> getItemById(Long itemId, Long userId);

    List<Item> getItemsByUser(Long userId);

    List<Item> getAvailableItems(Long userId, String text);

    void deleteItemById(Long itemId, Long userId);

    List<Item> getAllItems();
}

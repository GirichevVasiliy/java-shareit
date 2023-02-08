package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long ItemId = 1L;

    private Long getNextId() {
        return ItemId++;
    }
    @Override
    public Item addItem(Item item, User user) {

        return null;
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) {
        return null;
    }

    @Override
    public Item getItemById(Long itemId, Long userId) {
        return null;
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return null;
    }

    @Override
    public List<Item> getAvailableItems(Long userId, String text) {
        return null;
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {

    }

    @Override
    public List<Item> getAllItems() {
        return null;
    }
}

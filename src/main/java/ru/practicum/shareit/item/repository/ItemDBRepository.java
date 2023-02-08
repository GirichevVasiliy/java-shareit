package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public class ItemDBRepository implements ItemRepository{
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

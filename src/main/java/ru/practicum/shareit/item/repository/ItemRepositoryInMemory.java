package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository{

    @Override
    public Item addItem(Item item) {
        return null;
    }

    @Override
    public Item updateItem(Item item) {
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

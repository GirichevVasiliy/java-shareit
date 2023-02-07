package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
@Autowired
    public ItemServiceImpl(@Qualifier("itemRepositoryInMemory") ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

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

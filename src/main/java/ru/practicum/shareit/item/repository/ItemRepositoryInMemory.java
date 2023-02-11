package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    private Long getNextId() {
        return id++;
    }

    @Override
    public Item addItem(Item item, User user) {
        Long idItem = getNextId();
        item.setId(idItem);
        item.setOwner(user);
        items.put(idItem, item);
        return items.get(idItem);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) {
        Item oldItem = items.get(itemId);
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        if (item.getOwner() == null) {
            item.setOwner(oldItem.getOwner());
        }
        if (item.getRequest() == null) {
            item.setRequest(oldItem.getRequest());
        }
        item.setId(oldItem.getId());
        items.put(itemId, item);
        return items.get(itemId);
    }

    @Override
    public Optional<Item> getItemById(Long itemId, Long userId) {
        return Optional.ofNullable(items.get(itemId));

    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return items.values().stream().filter(i -> i.getOwner().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAvailableItems(Long userId, String text) {
        if (!text.isEmpty()) {
            return items.values().stream().filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase())
                    || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && i.getAvailable()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {
        items.remove(itemId);

    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}

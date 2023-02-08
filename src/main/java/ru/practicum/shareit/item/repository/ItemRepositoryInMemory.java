package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.RequestError;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Long id = getNextId();
        item.setId(id);
        item.setOwner(user);
        items.put(id, item);
        return items.get(id);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) {
        if (items.containsKey(itemId)) {
            if (items.get(itemId).getOwner().getId() == userId) {
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
            } else {
                throw new RequestError(HttpStatus.FORBIDDEN, "Пользователь ID " + userId + " пытался обновить вещь " + itemId + " данная вещь ему не принадлежит");
            }
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Вещь с ID " + itemId + " не найдена");
        }
    }

    @Override
    public Item getItemById(Long itemId, Long userId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Вещь с ID " + itemId + " не найдена, по запросу пользователя с ID " + userId);
        }
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return items.values().stream().filter(i -> i.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAvailableItems(Long userId, String text) {
        return items.values().stream().filter(i -> i.getName().contains(text) || i.getDescription().contains(text)).collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {
        if (items.containsKey(itemId)) {
            if (items.get(itemId).getOwner().getId() == userId) {
                items.remove(itemId);
            } else {
                throw new RequestError(HttpStatus.FORBIDDEN, "Пользователь ID " + userId + " пытался удалить вещь " + itemId + " данная вещь ему не принадлежит");
            }
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Вещь с ID " + itemId + " не найдена");
        }
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, UserDto userDto);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long iserId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByUser(Long userId);

    List<ItemDto> getAvailableItems(Long userId, String text);

    void deleteItemById(Long itemId, Long userId);

    List<ItemDto> getAllItems();
}

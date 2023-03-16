package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, UserDto userDto);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByUser(Long userId, Pageable pageable);

    List<ItemDto> getAvailableItems(Long userId, String text, Pageable pageable);

    void deleteItemById(Long itemId, Long userId);

    List<ItemDto> getAllItems();
}

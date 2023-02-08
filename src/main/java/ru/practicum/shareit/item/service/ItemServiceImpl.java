package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MapperItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.MapperUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemRepositoryInMemory") ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        log.info("Получен запрос на добавление вещи "+ itemDto.getName() + " от пользователя " + userDto.getEmail());
        User user = MapperUser.dtoToUser(userDto);
        Item item = MapperItem.toItem(itemDto);
        return MapperItem.toItemDto(itemRepository.addItem(item, user));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        log.info("Получен запрос на обновление вещи с ID "+ itemId + " от пользователя с ID " + userId);
        Item item = MapperItem.toItem(itemDto);
        return MapperItem.toItemDto(itemRepository.updateItem(itemId, item, userId));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        log.info("Получен запрос на поиск вещи с ID "+ itemId + " от пользователя с ID " + userId);
        return MapperItem.toItemDto(itemRepository.getItemById(itemId, userId));
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        log.info("Получен запрос на получение списока вещей пользователя с ID " + userId);
        return itemRepository.getItemsByUser(userId).stream().map(MapperItem::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAvailableItems(Long userId, String text) {
        log.info("Получен запрос на поиск вещи: "+ text + " от пользователя с ID " + userId);
        return itemRepository.getAvailableItems(userId, text).stream().map(MapperItem::toItemDto).collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {
        log.info("Получен запрос на удаление вещи с ID "+ itemId + " от пользователя с ID " + userId);
        itemRepository.deleteItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> getAllItems() {
        log.info("Получен запрос на получение списока всех вещей");
        return itemRepository.getAllItems().stream().map(MapperItem::toItemDto).collect(Collectors.toList());
    }
}

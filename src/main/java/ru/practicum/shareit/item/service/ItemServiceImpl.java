package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ForbiddenResourceException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemRepository;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemStorageImp") ItemStorage itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        log.info("Получен запрос на добавление вещи " + itemDto.getName() + " от пользователя " + userDto.getEmail());
        User user = UserMapper.dtoToUser(userDto);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemRepository.addItem(item, user));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        log.info("Получен запрос на обновление вещи с ID " + itemId + " от пользователя с ID " + userId);
        Item item = ItemMapper.toItem(itemDto);
        Optional<Item> itemFromBase = itemRepository.getItemById(itemId, userId);
        if (itemFromBase.isPresent()) {
            if (itemFromBase.get().getOwner().getId().equals(userId)) {
                return ItemMapper.toItemDto(itemRepository.updateItem(itemId, item, userId));
            } else {
                throw new ForbiddenResourceException("Пользователь ID " + userId + " пытался обновить вещь "
                        + itemId + " данная вещь ему не принадлежит");
            }
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена");
        }
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        log.info("Получен запрос на поиск вещи с ID " + itemId + " от пользователя с ID " + userId);
        Optional<Item> itemOptional = itemRepository.getItemById(itemId, userId);
        if (itemOptional.isPresent()) {
            return ItemMapper.toItemDto(itemOptional.get());
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена, по запросу " +
                    "пользователя с ID " + userId);
        }
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        log.info("Получен запрос на получение списока вещей пользователя с ID " + userId);
        return itemRepository.getItemsByUser(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAvailableItems(Long userId, String text) {
        log.info("Получен запрос на поиск вещи: " + text + " от пользователя с ID " + userId);
        return itemRepository.getAvailableItems(userId, text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {
        log.info("Получен запрос на удаление вещи с ID " + itemId + " от пользователя с ID " + userId);
        List<Item> itemsUser = itemRepository.getItemsByUser(userId);
        if (itemsUser.stream().anyMatch(i -> i.getId().equals(itemId))) {
            itemRepository.deleteItemById(itemId, userId);
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена");
        }
    }

    @Override
    public List<ItemDto> getAllItems() {
        log.info("Получен запрос на получение списка всех вещей");
        return itemRepository.getAllItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}

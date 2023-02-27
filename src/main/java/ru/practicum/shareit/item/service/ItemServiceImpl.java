package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.ForbiddenResourceException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemRepository") ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        log.info("Получен запрос на добавление вещи " + itemDto.getName() + " от пользователя " + userDto.getEmail());
        User user = UserMapper.dtoToUser(userDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }
    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        log.info("Получен запрос на обновление вещи с ID " + itemId + " от пользователя с ID " + userId);
        Item item = ItemMapper.toItem(itemDto);
        Optional<Item> itemFromBase = itemRepository.findById(itemId);
        if (itemFromBase.isPresent()) {
            if (itemFromBase.get().getOwner().getId().equals(userId)) {
                Item oldItem = itemFromBase.get();
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
                return ItemMapper.toItemDto(itemRepository.save(item));
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
        Optional<Item> itemOptional = itemRepository.findById(itemId);
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
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAvailableItems(Long userId, String text) {
        log.info("Получен запрос на поиск вещи: " + text + " от пользователя с ID " + userId);
        if (!text.isEmpty()) {
            return itemRepository.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    @Transactional
    @Override
    public void deleteItemById(Long itemId, Long userId) {
        log.info("Получен запрос на удаление вещи с ID " + itemId + " от пользователя с ID " + userId);
        List<Item> itemsUser = itemRepository.findByOwnerId(userId);
        if (itemsUser.stream().anyMatch(i -> i.getId().equals(itemId))) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена");
        }
    }

    @Override
    public List<ItemDto> getAllItems() {
        log.info("Получен запрос на получение списка всех вещей");
        return itemRepository.findAll().stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}

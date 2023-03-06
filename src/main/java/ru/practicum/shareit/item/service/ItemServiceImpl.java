package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.ForbiddenResourceException;
import ru.practicum.shareit.exception.exceptions.InvalidOwnerException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationDateException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService, CommentService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        log.info("Получен запрос на добавление вещи " + itemDto.getName() + " от пользователя " + userDto.getEmail());
        User user = UserMapper.dtoToUser(userDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDtoSingl(itemRepository.save(item));
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
                return ItemMapper.toItemDtoSingl(itemRepository.save(item));
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
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new ResourceNotFoundException(" Пользователь с " + userId + " не найден");
        }
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            return ItemMapper.toItemDtoFull(itemOptional.get(),
                    (itemOptional.get().getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())),
                    getLastBooking(itemOptional.get(), userId),
                    getNextBooking(itemOptional.get(), userId));
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена, по запросу " +
                    "пользователя с ID " + userId);
        }
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        log.info("Получен запрос на получение списка вещей пользователя с ID " + userId);
        return itemRepository.findByOwnerIdOrderById(userId).stream().map(item -> addCommentsAndDataTimeInItemDto(item, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAvailableItems(Long userId, String text) {
        log.info("Получен запрос на поиск вещи: " + text + " от пользователя с ID " + userId);
        if (!text.isEmpty()) {
            return itemRepository.getAvailableItems(text.toLowerCase()).stream()
                    .filter(Item::getAvailable)
                    .map(item -> ItemMapper.toItemDtoList(item,  item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())))
                    .collect(Collectors.toList());
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
        return itemRepository.findAll().stream().map(ItemMapper::toItemDtoSingl).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto addComment(Long itemId, Long authorId, CommentDto commentDto) {
        log.info("Получен запрос на добавление коммента");
        Optional<User> author = Optional.ofNullable(userRepository.findById(authorId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + authorId + " не найден")));
        Optional<Item> item = Optional.ofNullable(itemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException(" Вещь с " + itemId + " не найдена")));
        isBooker(item.get(), author.get());
        isTheBookingEnd(item.get(), author.get());
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto, author.get(), item.get());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemDto addCommentsAndDataTimeInItemDto(Item item, Long userId) {
        return ItemMapper.toItemDtoFull(item,
                (item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())),
                getLastBooking(item, userId),
                getNextBooking(item, userId));
    }
    private DateBookingDto getLastBooking(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) {
            return null;
        }
        Optional<Booking> bookings = bookingRepository.findByItemAndEndIsBeforeOrderByStart(item, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookings.get());
    }

    private DateBookingDto getNextBooking(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) {
            return null;
        }
        Optional<Booking> bookings = bookingRepository.findByItemAndStartIsAfterAndStatusIsNotOrderByStar(item.getId(),
                LocalDateTime.now(), StatusBooking.REJECTED.name());
        if (bookings.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookings.get());
    }

    private void isBooker(Item item, User user) {
        boolean isBooker = bookingRepository.findByBookerOrderByStartDesc(user).stream()
                .anyMatch(booking -> booking.getItem().equals(item));
        if (!isBooker) {
            throw new InvalidOwnerException("Пользователь с id " + user.getId() + "никогда ее не бронировал вещь c Id " +
                    item.getId() + " комментарии не доступны.");
        }
    }

    private void isTheBookingEnd(Item item, User user) {
        boolean isEnd = bookingRepository.findByBookerAndItem(user, item).stream()
                .anyMatch((booking) -> booking.getEnd().isBefore(LocalDateTime.now()));
        if (!isEnd) {
            throw new ValidationDateException("Невозможно оставить коммент. Бронирование не завершено");
        }
    }

}

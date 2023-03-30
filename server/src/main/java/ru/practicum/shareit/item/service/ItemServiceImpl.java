package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenResourceException;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.ValidationDateBookingException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService, CommentService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        log.info("Получен запрос на добавление вещи " + itemDto.getName() + " от пользователя " + userDto.getEmail());
        User user = UserMapper.dtoToUser(userDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            item.setRequest(getItemRequest(itemDto.getRequestId()));
        }
        return ItemMapper.toItemDtoSingl(itemRepository.save(item));
    }


    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        log.info("Получен запрос на обновление вещи с ID " + itemId + " от пользователя с ID " + userId);
        Item item = ItemMapper.toItem(itemDto);
        if (itemDto.getRequestId() != null) {
            item.setRequest(getItemRequest(itemDto.getRequestId()));
        }
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
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(" Пользователь с " + userId + " не найден");
        }
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            List<Booking> allBookings = bookingRepository.findAllByItemIdAndStatus(itemId, StatusBooking.APPROVED);
            return ItemMapper.toItemDtoFull(itemOptional.get(),
                    (itemOptional.get().getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())),
                    getLastBookingList(itemOptional.get(), userId, allBookings),
                    getNextBookingList(itemOptional.get(), userId, allBookings));
        } else {
            throw new ResourceNotFoundException("Вещь с ID " + itemId + " не найдена, по запросу " +
                    "пользователя с ID " + userId);
        }
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId, Pageable pageable) {
        log.info("Получен запрос на получение списка вещей пользователя с ID " + userId);
        List<Item> items = itemRepository.findByOwnerIdOrderById(userId, pageable).getContent();
        List<Booking> allBookings = bookingRepository.findAllByItemIdInAndStatus(items.stream().map(Item::getId)
                .collect(Collectors.toList()), StatusBooking.APPROVED, pageable).getContent();
        Map<Long, List<Booking>> byItemIdList = allBookings.stream().collect(Collectors.groupingBy(b -> b.getItem().getId()));
        List<ItemDto> result = new LinkedList<>();
        for (Item item : items) {
            List<Booking> bookings = byItemIdList.get(item.getId());
            result.add(ItemMapper.toItemDtoFull(item,
                    (item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())),
                    getLastBookingList(item, userId, bookings),
                    getNextBookingList(item, userId, bookings)));
        }
        return result;
    }

    @Override
    public List<ItemDto> getAvailableItems(Long userId, String text, Pageable pageable) {
        log.info("Получен запрос на поиск вещи: " + text + " от пользователя с ID " + userId);
        if (!text.isEmpty()) {
            return itemRepository.getAvailableItems(text.toLowerCase(), pageable).getContent().stream()
                    .map(item -> ItemMapper.toItemDtoList(item, item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())))
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
        checkBooker(item.get(), author.get());
        checkIfBookingCompleted(item.get(), author.get());
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto, author.get(), item.get());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private DateBookingDto getLastBookingList(Item item, Long userId, List<Booking> bookings) {
        if (!item.getOwner().getId().equals(userId) || bookings == null) {
            return null;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        Optional<Booking> bookingLast = bookings.stream().sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(b -> b.getStart().isBefore(nowTime)).findFirst();
        if (bookingLast.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookingLast.get());
    }

    private DateBookingDto getNextBookingList(Item item, Long userId, List<Booking> bookings) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (!item.getOwner().getId().equals(userId) || bookings == null) {
            return null;
        }
        Optional<Booking> bookingNext = bookings.stream().sorted(Comparator.comparing(Booking::getStart))
                .filter(b -> b.getStart().isAfter(nowTime)).findFirst();
        if (bookingNext.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookingNext.get());
    }

    private void checkBooker(Item item, User user) {
        final int limit = 1;
        boolean isBooker = bookingRepository.bookingСonfirmation(user.getId(), item.getId(), limit).isPresent();
        if (!isBooker) {
            throw new InvalidOwnerException("Пользователь с id " + user.getId() + "никогда ее не бронировал вещь c Id " +
                    item.getId() + " комментарии не доступны.");
        }
    }

    private void checkIfBookingCompleted(Item item, User user) {
        final int limit = 1;
        Optional<Booking> bookingNotEnd = bookingRepository.findByBookerAndItem(item.getId(), user.getId(), LocalDateTime.now(), StatusBooking.REJECTED.name(), limit);
        boolean isEnd = bookingNotEnd.isPresent();
        if (!isEnd) {
            throw new ValidationDateBookingException("Невозможно оставить коммент. Бронирование не завершено");
        }
    }

    private ItemRequest getItemRequest(Long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        return itemRequest.get();
    }
}
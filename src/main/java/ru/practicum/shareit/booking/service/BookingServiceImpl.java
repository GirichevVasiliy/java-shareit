package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(@Qualifier("bookingRepository") BookingRepository bookingRepository,
                              @Qualifier("userRepository") UserRepository userRepository,
                              @Qualifier("itemRepository") ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    @Override
    public BookingDto addBooking(InputBookingDto inputBookingDto, Long userId) {
        log.info("Получен запрос на бронирование вещи от пользователя с Id" + userId);
        if (checkDate(inputBookingDto.getStart(), inputBookingDto.getEnd())) {
            Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException(" Пользователь с Id " + userId + " не найден")));
            Optional<Item> item = Optional.ofNullable(itemRepository.findById(inputBookingDto.getItemId()).orElseThrow(
                    () -> new ResourceNotFoundException(" Вещь с Id " + inputBookingDto.getItemId() + " не найдена")));
            if (!item.get().getAvailable()) {
                throw new ValidationAvailableException("Бронирование не доступно");
            }
            if (userId.equals(item.get().getOwner().getId())) {
                throw new ValidationOwnerException("Владельцу нельзя забронировать свою вещь");
            }
            Booking booking = BookingMapper.createNewBooking(inputBookingDto, user.get(), item.get());
            return BookingMapper.bookingToDto(bookingRepository.save(booking));
        } else {
            throw new ValidationDateException(" Пользователь с id " + userId + " задал не верное время бронирования");
        }
    }

    @Transactional
    @Override
    public BookingDto updateApprove(Long bookingId, Boolean approved, Long userId) {
        log.info("Получен запрос подтверждение или отклонение запроса на бронирование Id " + bookingId
                + " от пользователя с Id " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException(" Бронирование с " + bookingId + " не найдено")));
        if (!user.get().getId().equals(booking.get().getItem().getOwner().getId())) {
            throw new ValidationOwnerException(" Пользователь с id " + userId + " не владелец вещи");
        }
        if (booking.get().getStatus() != StatusBooking.WAITING) {
            throw new ValidationStateException("Статус должен быть WAITING");
        }
        if (approved) {
            booking.get().setStatus(StatusBooking.APPROVED);
        } else {
            booking.get().setStatus(StatusBooking.REJECTED);
        }
        return BookingMapper.bookingToDto(bookingRepository.save(booking.get()));
    }


    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        log.info("Получен запрос на просмотр бронирования с Id " + bookingId
                + " от пользователя с Id " + userId);
        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException(" Бронирование с " + bookingId + " не найдено")));
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        User owner = booking.get().getItem().getOwner();
        User booker = booking.get().getBooker();
        if (!(owner.getId().equals(user.get().getId()) || booker.getId().equals(user.get().getId()))) {
            throw new ValidationOwnerException("Просмотр бронирования доспупен владельцу вещи или бронирующему");
        }
        return BookingMapper.bookingToDto(booking.get());
    }

    @Override
    public List<BookingDto> getAllBookings(Long userId, StateBooking stateBooking) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        switch (stateBooking) {
            case ALL:
                return bookingRepository.findByBooker(user.get()).stream().map(BookingMapper::bookingToDto)
                        .sorted(comparing(BookingDto::getStart).reversed()).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrentByBooker(user.get())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBefore(user.get(), LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatus(user.get(), StatusBooking.WAITING)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerAndStartIsAfter(user.get(), LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatus(user.get(), StatusBooking.REJECTED)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new ValidationStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsForOwner(Long ownerId, StateBooking stateBooking) {
        Optional<User> owner = Optional.ofNullable(userRepository.findById(ownerId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + ownerId + " не найден")));
        List<Item> items = itemRepository.findByOwnerId(owner.get().getId());
        return items.stream()
                .flatMap((item) -> getAllBookingsForOwnerState(item, stateBooking).stream())
                .collect(Collectors.toList());
    }

    private List<BookingDto> getAllBookingsForOwnerState(Item item, StateBooking stateBooking) {
        switch (stateBooking) {
            case ALL:
                return bookingRepository.findByItem(item).stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrentByItem(item)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findByItemAndEndIsBefore(item, LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByItemAndStatus(item, StatusBooking.WAITING)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByItemAndStartIsAfter(item, LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByItemAndStatus(item, StatusBooking.REJECTED)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new ValidationStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private boolean checkDate(LocalDateTime startBooking, LocalDateTime endBooking) {
        if (startBooking.isBefore(LocalDateTime.now()) || endBooking.isBefore(LocalDateTime.now()) ||
                endBooking.isBefore(startBooking)) {
            return false;
        }
        return true;
    }
}

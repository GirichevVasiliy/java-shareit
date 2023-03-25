package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<BookingDto> getAllBookings(Long userId, StateBooking stateBooking, Pageable pageable) {
        Pageable pageableNew = checkPageable(pageable);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        switch (stateBooking) {
            case ALL:
                return bookingRepository.findAllByBooker(user.get(), pageableNew).getContent().stream()
                        .map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrent(user.get(), pageableNew).getContent()
                        .stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBefore(user.get(), LocalDateTime.now(), pageableNew).getContent()
                        .stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatus(user.get(), StatusBooking.WAITING, pageableNew).getContent()
                        .stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerAndStartIsAfter(user.get(), LocalDateTime.now(), pageableNew).getContent()
                        .stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatus(user.get(), StatusBooking.REJECTED, pageableNew).getContent()
                        .stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            default:
                throw new ValidationStateException("Unknown state: " + stateBooking);
        }
    }

    @Override
    public List<BookingDto> getAllBookingsForOwner(Long ownerId, StateBooking stateBooking, Pageable pageable) {
        Optional<User> owner = Optional.ofNullable(userRepository.findById(ownerId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + ownerId + " не найден")));
        List<Booking> bookings = bookingRepository.findAllByOwner(ownerId, pageable);
        return getAllBookingsForOwnerState(bookings, stateBooking);
    }

    private List<BookingDto> getAllBookingsForOwnerState(List<Booking> bookings, StateBooking stateBooking) {
        LocalDateTime now = LocalDateTime.now();
        switch (stateBooking) {
            case ALL:
                return bookings.stream().map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case CURRENT:
                return bookings.stream()
                        .filter(b -> now.isAfter(b.getStart()) && now.isBefore(b.getEnd())).map(BookingMapper::bookingToDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream().filter(b -> now.isAfter(b.getEnd())).map(BookingMapper::bookingToDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(StatusBooking.WAITING)).map(BookingMapper::bookingToDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(b -> now.isBefore(b.getStart())).map(BookingMapper::bookingToDto).collect(Collectors.toList());
            case REJECTED:
                return bookings.stream().filter(b -> b.getStatus().equals(StatusBooking.REJECTED)).map(BookingMapper::bookingToDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationStateException("Unknown state: " + stateBooking);
        }
    }

    private boolean checkDate(LocalDateTime startBooking, LocalDateTime endBooking) {
        if (endBooking.isBefore(startBooking)) {
            return false;
        }
        if (endBooking.equals(startBooking)) {
            return false;
        }
        if (startBooking.isBefore(LocalDateTime.now()) || endBooking.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    private Pageable checkPageable(Pageable pageable) {
        if ((pageable.getPageNumber() + 1) / (pageable.getPageSize()) > 0 && ((pageable.getPageNumber() + 1) % (pageable.getPageSize()) == 0)) {
            Integer i = ((pageable.getPageNumber() + 1) / pageable.getPageSize()) - 1;
            return PageRequest.of(i, pageable.getPageSize(), Sort.by("start").descending());
        } else if ((pageable.getPageNumber() + 1) / (pageable.getPageSize()) > 0 && ((pageable.getPageNumber() + 1) % (pageable.getPageSize()) != 0)) {
            Integer i = ((pageable.getPageNumber() + 1) / pageable.getPageSize());
            return PageRequest.of(i, pageable.getPageSize(), Sort.by("start").descending());
        }
        return pageable;
    }
}

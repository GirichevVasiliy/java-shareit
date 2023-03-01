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
import ru.practicum.shareit.booking.model.enam.StateBooking;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationDateException;
import ru.practicum.shareit.exception.exceptions.ValidationOwnerException;
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
        if (checkdate(inputBookingDto.getStart(), inputBookingDto.getEnd())) {
            log.info("Получен запрос на бронирование вещи от пользователя с Id" + userId);
            Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
            Optional<Item> item = Optional.ofNullable(itemRepository.findById(inputBookingDto.getItemId()).orElseThrow(
                    () -> new ResourceNotFoundException(" Вещь с " + inputBookingDto.getItemId() + " не найдена")));
            Booking booking = BookingMapper.createNewBooking(inputBookingDto, user.get(), item.get());
            return BookingMapper.bookingToDto(bookingRepository.save(booking));
        } else {
            throw new ValidationDateException(" Пользователь с id " + userId + " задал не верное время бронирования");
        }
    }

    @Transactional
    @Override
    public BookingDto updateApprove(Long bookingId, Boolean approved, Long userId) {
        log.info("Получен запрос подтверждение или отклонение запроса на бронирование " + bookingId
                + " от пользователя с Id " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException(" Бронирование с " + bookingId + " не найдено")));
        if (!user.get().getId().equals(booking.get().getItem().getOwner().getId())) {
            throw new ValidationOwnerException(" Пользователь с id " + userId + " не владелец вещи");
        } else {
            if (approved) {
                booking.get().setStatus(StatusBooking.APPROVED);
            } else {
                booking.get().setStatus(StatusBooking.REJECTED);
            }
            return BookingMapper.bookingToDto(bookingRepository.save(booking.get()));
        }
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException(" Бронирование с " + bookingId + " не найдено")));
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        if (!user.get().getId().equals(booking.get().getItem().getOwner().getId()) ||
                !user.get().getId().equals(booking.get().getBooker().getId())) {
            throw new ValidationOwnerException(" Пользователь с id " + userId + " не владелец вещи или не автор брони");
        } else {
            return BookingMapper.bookingToDto(booking.get());
        }
    }

    @Override
    public List<BookingDto> getAllBookings(Long userId, StateBooking stateBooking) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));

        switch (stateBooking) {
            case ALL:
                return bookingRepository.findAll().stream().map(BookingMapper::bookingToDto)
                        .sorted(comparing(BookingDto::getStart).reversed()).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBookerAndStartIsBefore(user.get(), LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBefore(user.get(), LocalDateTime.now())
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatusIsContainingIgnoreCase(user.get(), StatusBooking.WAITING)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case FUTURE:

            case REJECTED:
                return bookingRepository.findAllByBookerAndStatusIsContainingIgnoreCase(user.get(), StatusBooking.REJECTED)
                        .stream().map(BookingMapper::bookingToDto).sorted(comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Не существующий статус");
    }

    @Override
    public List<BookingDto> getAllBookingsForOwner(Long ownerId, StatusBooking statusBooking) {
        return null;
    }

    private boolean checkdate(LocalDateTime startBooking, LocalDateTime endBooking) {
        if (startBooking.isBefore(LocalDateTime.now()) || endBooking.isBefore(LocalDateTime.now()) ||
                endBooking.isBefore(startBooking) || startBooking.isEqual(endBooking)) {
            return false;
        }
        return true;
    }
}

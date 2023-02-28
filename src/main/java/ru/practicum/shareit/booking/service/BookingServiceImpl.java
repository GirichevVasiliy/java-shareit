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
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

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
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        Optional<Item> item = Optional.ofNullable(itemRepository.findById(inputBookingDto.getItemId()).orElseThrow(
                () -> new ResourceNotFoundException(" Вещь с " + inputBookingDto.getItemId() + " не найдена")));
        Booking booking = BookingMapper.createNewBooking(inputBookingDto, user.get(), item.get());
        return BookingMapper.bookingToDto(bookingRepository.save(booking));
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
            if (approved){
                booking.get().setStatus(StatusBooking.APPROVED);
            } else {
                booking.get().setStatus(StatusBooking.REJECTED);
            }
           return  BookingMapper.bookingToDto(bookingRepository.save(booking.get()));
        }
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookings(Long bookingId, StatusBooking statusBooking) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsForOwner(Long ownerId, StatusBooking statusBooking) {
        return null;
    }
}

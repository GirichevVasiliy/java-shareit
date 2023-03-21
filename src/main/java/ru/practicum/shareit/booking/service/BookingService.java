package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(InputBookingDto inputBookingDto, Long userId);

    BookingDto updateApprove(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookings(Long userId, StateBooking stateBooking, Pageable pageable);

    List<BookingDto> getAllBookingsForOwner(Long ownerId, StateBooking stateBooking, Pageable pageable);
}

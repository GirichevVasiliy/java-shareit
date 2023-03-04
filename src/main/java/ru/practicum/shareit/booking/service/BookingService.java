package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.enam.StateBooking;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(InputBookingDto inputBookingDto, Long userId);

    BookingDto updateApprove(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookings(Long userId, StateBooking stateBooking);

    List<BookingDto> getAllBookingsForOwner(Long ownerId, StateBooking stateBooking);
}

package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.StateBooking;
import ru.practicum.shareit.util.PageableFactory;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody InputBookingDto inputBookingDto) {
        return bookingService.addBooking(inputBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateApprove(@PathVariable Long bookingId,
                                    @RequestParam Boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.updateApprove(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateBooking state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookings(userId, state, PageableFactory.getPageableSortDescStart(from, size));
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateBooking state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsForOwner(userId, state, PageableFactory.getPageableSortDescStart(from, size));
    }
}

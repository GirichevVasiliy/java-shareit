package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.StateBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(ItemService itemService, UserService userService, BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId, @Valid @RequestBody InputBookingDto inputBookingDto) {
        return bookingService.addBooking(inputBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateApprove(@PathVariable Long bookingId,
                                    @RequestParam @NotNull Boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingService.updateApprove(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateBooking state) {
        return bookingService.getAllBookings(userId, state);
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateBooking state) {
        return bookingService.getAllBookingsForOwner(userId, state);
    }

}

package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    @Autowired
    public BookingController(ItemService itemService, UserService userService, BookingService bookingService) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId, @Valid @RequestBody InputBookingDto inputBookingDto) {
        return bookingService.addBooking(inputBookingDto, userId);
    }
    @PatchMapping("/{bookingId}")
    public BookingDto updateApprove( @PathVariable Long bookingId,
                                     @RequestParam @NotNull Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") @NotNull Long userId){
        return bookingService.updateApprove(bookingId, approved, userId);
    }







    /*

    BookingDto updateApprove(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookings(Long bookingId, StatusBooking statusBooking);

    List<BookingDto> getAllBookingsForOwner(Long ownerId, StatusBooking statusBooking);*/




}

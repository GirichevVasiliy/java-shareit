package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private StatusBooking status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = StatusBooking.WAITING;
    }

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker, StatusBooking status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    public BookingDto(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = StatusBooking.WAITING;
    }
}

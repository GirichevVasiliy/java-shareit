package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private final Long id;
    @JsonFormat
    private final LocalDateTime start;
    @JsonFormat
    private final LocalDateTime end;
    private final ItemDto item;
    private final UserDto booker;
    private final StatusBooking status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDto itemDto, UserDto bookerDto, StatusBooking status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = itemDto;
        this.booker = bookerDto;
        this.status = status;
    }
}

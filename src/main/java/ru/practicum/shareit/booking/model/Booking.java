package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.enam.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private LocalDateTime start; // дата и время начала бронирования;
    private LocalDateTime end; // дата и время конца бронирования;
    private Item item; //вещь, которую пользователь бронирует;
    private User booker; // пользователь, который осуществляет бронирование;
    private StatusBooking status; // статус бронирования. Может принимать одно из следующих
}

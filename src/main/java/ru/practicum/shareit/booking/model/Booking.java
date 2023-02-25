package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start; // дата и время начала бронирования;
    @Column(name = "end_date")
    private LocalDateTime end; // дата и время конца бронирования;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item; //вещь, которую пользователь бронирует;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker; // пользователь, который осуществляет бронирование;
    @Enumerated(EnumType.STRING)
    private StatusBooking status; // статус бронирования. Может принимать одно из следующих
    @ManyToMany
    List<User> users = new ArrayList<>();
    public Booking(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
    }

    public Booking() {
    }
}


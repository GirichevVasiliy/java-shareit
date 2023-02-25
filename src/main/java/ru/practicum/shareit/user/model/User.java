package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String name;
    @OneToMany(mappedBy = "booker")
    List<Booking> bookings;
    @OneToMany(mappedBy = "owner")
    List<Item> items;

    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }
}

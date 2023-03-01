package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatusIsContainingIgnoreCase(User booker, StatusBooking status);
    List<Booking> findAllByBookerAndStartIsBefore(User booker, LocalDateTime localDateTimeNow);
    List<Booking> findAllByBookerAndEndIsBefore(User booker, LocalDateTime localDateTimeNow);
}

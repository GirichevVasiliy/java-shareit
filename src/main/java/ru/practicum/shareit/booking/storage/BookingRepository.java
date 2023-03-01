package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enam.StateBooking;
import ru.practicum.shareit.booking.model.enam.StatusBooking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdAndAndStatusIsContainingIgnoreCase(Long userId, StatusBooking status);
}

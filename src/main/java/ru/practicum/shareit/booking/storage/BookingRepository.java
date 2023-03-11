package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, StatusBooking status);

    List<Booking> findByBookerOrderByStartDesc(User booker);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.booker_id = :bookerId AND " +
            "b.item_id = :itemId ORDER BY b.start_date ASC LIMIT :limit", nativeQuery = true)
    Optional<Booking> bookingСonfirmation(Long bookerId, Long itemId, Integer limit);

    @Query("select b from Booking b where b.booker = :booker and b.start <= current_timestamp and b.end >= current_timestamp ORDER BY b.start DESC")
    List<Booking> findCurrent(User booker);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findAllByItemIdAndStatus(Long itemId, StatusBooking status);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.item_id = :itemId AND " +
            "b.end_date < :currentTime AND b.status != :status ORDER BY b.start_date ASC LIMIT :limit", nativeQuery = true)
    Optional<Booking> findByItemAndEndIsBefore(Long itemId, LocalDateTime currentTime, String status, Integer limit);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.item_id = :itemId AND " +
            "b.start_date > :currentTime AND b.status != :status ORDER BY b.start_date ASC LIMIT :limit", nativeQuery = true)
    Optional<Booking> findByItemAndStartIsAfter(Long itemId, LocalDateTime currentTime, String status, Integer limit);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.item_id = :itemId AND b.booker_id = :bookerId " +
            "AND b.end_date < :currentTime AND  b.status != :status LIMIT :limit", nativeQuery = true)
    Optional<Booking> findByBookerAndItem(Long itemId, Long bookerId, LocalDateTime currentTime, String status, Integer limit);

    @Query("SELECT b FROM Booking b INNER JOIN Item i ON i.id = b.item.id WHERE i.owner.id = :ownerId ORDER BY b.start DESC")
    List<Booking> findAllByOwner(Long ownerId);

    List<Booking> findAllByItemIdInAndStatus(List<Long> ids, StatusBooking status);
}

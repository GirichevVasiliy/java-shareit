package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Booking> findAllByBookerAndStatus(User booker, StatusBooking status, Pageable pageable);

    Page<Booking> findAllByBooker(User booker, Pageable pageable);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.booker_id = :bookerId AND " +
            "b.item_id = :itemId ORDER BY b.start_date ASC LIMIT :limit", nativeQuery = true)
    Optional<Booking> bookingСonfirmation(Long bookerId, Long itemId, Integer limit);

    @Query("select b from Booking b where b.booker = :booker and b.start <= current_timestamp and b.end >= current_timestamp")
    Page<Booking> findCurrent(User booker, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime localDateTimeNow, Pageable pageable);

    Page<Booking> findAllByBookerAndEndIsBefore(User booker, LocalDateTime localDateTimeNow, Pageable pageable);

    List<Booking> findAllByItemIdAndStatus(Long itemId, StatusBooking status);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.item_id = :itemId AND b.booker_id = :bookerId " +
            "AND b.end_date < :currentTime AND  b.status != :status LIMIT :limit", nativeQuery = true)
    Optional<Booking> findByBookerAndItem(Long itemId, Long bookerId, LocalDateTime currentTime, String status, Integer limit);

    @Query("SELECT b FROM Booking b INNER JOIN Item i ON i.id = b.item.id WHERE i.owner.id = :ownerId")
    List<Booking> findAllByOwner(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItemIdInAndStatus(List<Long> ids, StatusBooking status, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end")
    List<Booking> findAllCurrentByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP > b.end")
    List<Booking> findAllPastByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id WHERE i.owner.id = :ownerId AND b.status = :status")
    List<Booking> findAllWaitingByOwnerId(Long ownerId,  StatusBooking status, Pageable pageable);
    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP < b.start")
    List<Booking> findAllFutureByOwnerId(Long ownerId, Pageable pageable);
    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id WHERE i.owner.id = :ownerId AND b.status = :status")
    List<Booking> findAllRejectedByOwnerId(Long ownerId, StatusBooking status, Pageable pageable);
}

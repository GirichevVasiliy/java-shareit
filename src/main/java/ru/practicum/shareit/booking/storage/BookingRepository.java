package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, StatusBooking status);

    List<Booking> findByBookerOrderByStartDesc(User booker);

    @Query("select b from Booking b where b.booker = :booker and b.start <= current_timestamp and b.end >= current_timestamp")
    List<Booking> findCurrentByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findByItemOrderByStartDesc(Item item);

    @Query("select b from Booking b where b.item = :item and b.start <= current_timestamp and b.end >= current_timestamp")
    List<Booking> findCurrentByItemOrderByStartDesc(Item item);

    List<Booking> findByItemAndEndIsBeforeOrderByStartDesc(Item item, LocalDateTime end);

    Optional<Booking> findByItemAndEndIsBeforeOrderByStart(Item item, LocalDateTime end);

    @Query(value = "SELECT * FROM bookings b JOIN items i ON i.id = b.item_id WHERE b.item_id = :itemId AND " +
            "b.start_date > :currentTime AND b.status != :status ORDER BY b.start_date ASC LIMIT 1", nativeQuery = true)
    Optional<Booking> findByItemAndStartIsAfterAndStatusIsNotOrderByStar(Long itemId, LocalDateTime currentTime, String status);

    List<Booking> findByItemAndStartIsAfterOrderByStartDesc(Item item, LocalDateTime start);

    List<Booking> findByItemAndStatusOrderByStartDesc(Item item, StatusBooking status);

    List<Booking> findByBookerAndItem(User booker, Item item);
















   /* @Query("select b from Booking b where b.item.id in (select i.id from Item i where i.owner = ?5)")
    List<Booking> findAllBookingsByOwner(User owner);

    @Query("select b from Booking b where b.item.id in (select i.id from Item i where i.owner = ?5) and b.status = ?6")
    List<Booking> findAllBookingsByOwnerStatus(User owner, StatusBooking status);

    @Query("select b from Booking b where b.item.id in (select i.id from Item i where i.owner = ?5) and b.start <= current_timestamp and b.end >= current_timestamp")
    List<Booking> findAllByBookerIsCurrentItem(User booker, LocalDateTime localDateTimeNow);

    @Query("select b from Booking b where b.item.id in (select i.id from Item i where i.owner = ?5) and b.start >= current_timestamp")
    List<Booking> findAllByBookerAndStartIsAfterForItems(User booker, LocalDateTime localDateTimeNow);

    @Query("select b from Booking b where b.item.id in (select i.id from Item i where i.owner = ?5) and b.end <= current_timestamp")
    List<Booking> findAllByBookerAndStartIsBeforeForItems(User booker, LocalDateTime localDateTimeNow);*/
}

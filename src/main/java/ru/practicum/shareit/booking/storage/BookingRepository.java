package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enam.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatus(User booker, StatusBooking status);

    List<Booking> findByBooker(User booker);

    @Query("select b from Booking b where b.booker = :booker and b.start <= current_timestamp and b.end >= current_timestamp")
    List<Booking> findCurrentByBooker(User booker);

    List<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findAllByBookerAndEndIsBefore(User booker, LocalDateTime localDateTimeNow);

    List<Booking> findByItem(Item item);

    @Query("select b from Booking b where b.item = :item and b.start <= current_timestamp and b.end >= current_timestamp")
    List<Booking> findCurrentByItem(Item item);

    List<Booking> findByItemAndEndIsBefore(Item item, LocalDateTime end);

    List<Booking> findByItemAndStartIsAfter(Item item, LocalDateTime start);

    List<Booking> findByItemAndStatus(Item item, StatusBooking status);


















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

package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CommentRepository commentRepository;
    private User firstUser;
    final Long userId1 = 1L;
    final Long userId2 = 2L;
    final Long bookingId = 1L;
    final Long itemId = 1L;
    private ItemRequest firstItemRequest;
    private ItemRequest secondItemRequest;
    private Booking firstBooking;
    private Booking secondBooking;
    private Booking fourthBooking;
    private Booking thirdBooking;
    private Item firstItem;
    private Item secondItem;
    private User onwer;
    private User oldUser;
    private Booking oldBooking;
    private User secondUser;
    private Comment comment;
    final Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());
    final int size = 0;
    private Page<Booking> page = new PageImpl<>(new ArrayList<>(), pageable, size);

    @BeforeEach
    private void init() {
        firstUser = userRepository.save(new User(1L, "user1", "y1@email.ru"));
        entityManager.persist(firstUser);

        onwer = userRepository.save(new User(2L, "user2", "y2@email.ru"));
        entityManager.persist(onwer);

        secondUser = userRepository.save(new User(3L, "user3", "y3@email.ru"));
        entityManager.persist(secondUser);

        oldUser = userRepository.save(new User(4L, "user4", "y4@email.ru"));
        entityManager.persist(oldUser);

        firstItemRequest = itemRequestRepository.save(new ItemRequest(1L, "text", firstUser,
                LocalDateTime.parse("2024-10-23T17:19:33")));
        entityManager.persist(firstItemRequest);

        secondItemRequest = itemRequestRepository.save(new ItemRequest(2L, "text2", secondUser,
                LocalDateTime.parse("2024-10-23T17:19:55")));
        entityManager.persist(secondItemRequest);


        firstItem = itemRepository.save(new Item(1L, "item1", "text", true, onwer, firstItemRequest));
        entityManager.persist(firstItem);

        secondItem = itemRepository.save(new Item(2L, "item2", "text2", true, onwer, firstItemRequest));
        entityManager.persist(secondItem);

        comment = commentRepository.save(new Comment(1L, "comment", firstItem, secondUser, LocalDateTime.now()));
        entityManager.persist(comment);

        firstBooking = bookingRepository.save(new Booking(1L, LocalDateTime.parse("2024-10-23T17:19:33"),
                LocalDateTime.parse("2024-10-23T17:19:45"), firstItem, firstUser, StatusBooking.WAITING));
        entityManager.persist(firstBooking);

        secondBooking = bookingRepository.save(new Booking(2L, LocalDateTime.parse("2025-10-23T17:11:23"),
                LocalDateTime.parse("2025-10-23T17:12:45"), firstItem, firstUser, StatusBooking.APPROVED));
        entityManager.persist(secondBooking);
        thirdBooking = bookingRepository.save(new Booking(3L, LocalDateTime.parse("2025-10-23T17:11:00"),
                LocalDateTime.parse("2025-10-23T17:12:05"), firstItem, firstUser, StatusBooking.CANCELED));
        entityManager.persist(secondBooking);
        fourthBooking = bookingRepository.save(new Booking(4L, LocalDateTime.parse("2022-09-11T11:11:00"),
                LocalDateTime.parse("2024-10-23T17:12:05"), firstItem, firstUser, StatusBooking.REJECTED));
        entityManager.persist(secondBooking);
        oldBooking = bookingRepository.save(new Booking(5L, LocalDateTime.parse("2022-09-11T11:11:00"),
                LocalDateTime.parse("2022-10-23T17:12:05"), firstItem, oldUser, StatusBooking.REJECTED));
        entityManager.persist(secondBooking);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    private void afterTest() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRequestRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void findAllByBookerAndStatus_whenFindFirstUserAndStatusWAITING_thenReturnFirstBooking() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(firstUser, StatusBooking.WAITING, pageable).getContent();
        assertThat(bookings.contains(firstBooking));
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondtUserAndStatusAPPROVED_thenReturnSecondBooking() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(firstUser, StatusBooking.APPROVED, pageable).getContent();
        assertThat(bookings.contains(secondBooking));
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondtUserAndStatusCANCELED_thenReturnThirdBooking() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(firstUser, StatusBooking.CANCELED, pageable).getContent();
        assertThat(bookings.contains(thirdBooking));
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondtUserAndStatusREJECTED_thenReturnFourthBooking() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(firstUser, StatusBooking.REJECTED, pageable).getContent();
        assertThat(bookings.contains(fourthBooking));
    }

    @Test
    public void findAllByBookerAndStatus_whenOnwertUserAndStatusREJECTED_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(onwer, StatusBooking.REJECTED, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondUserAndStatusREJECTED_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(secondUser, StatusBooking.APPROVED, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondUserAndStatusCANCELED_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(secondUser, StatusBooking.CANCELED, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBookerAndStatus_whenSecondUserAndStatusWAITING_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStatus(secondUser, StatusBooking.WAITING, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBooker_whenFirstUser_thenReturnListBooking() {
        final int size = 4;
        List<Booking> bookings = bookingRepository.findAllByBooker(firstUser, pageable).getContent();
        assertThat(bookings.size() == size).isTrue();
        assertThat(bookings.contains(firstBooking)).isTrue();
        assertThat(bookings.contains(secondBooking)).isTrue();
        assertThat(bookings.contains(thirdBooking)).isTrue();
        assertThat(bookings.contains(fourthBooking)).isTrue();
    }

    @Test
    public void findAllByBooker_whenSecondUser_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findAllByBooker(secondUser, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void bookingСonfirmation_whenFourthBooking_thenReturnBooking() {
        final int limit = 1;
        Optional<Booking> booking = bookingRepository.bookingСonfirmation(bookingId, itemId, limit);
        assertThat(booking.get().equals(fourthBooking)).isTrue();
    }

    @Test
    public void bookingСonfirmation_whenBadBookingId_thenReturnBooking() {
        final int limit = 1;
        Long bookingId = 99L;
        Optional<Booking> booking = bookingRepository.bookingСonfirmation(bookingId, itemId, limit);
        assertThat(booking.isEmpty()).isTrue();
    }

    @Test
    public void findCurrent_whenFirstUser_thenReturnFourthBooking() {
        List<Booking> bookings = bookingRepository.findCurrent(firstUser, pageable).getContent();
        assertThat(bookings.contains(fourthBooking)).isTrue();
    }

    @Test
    public void findCurrent_whenSecondUser_thenReturnListEmpty() {
        List<Booking> bookings = bookingRepository.findCurrent(secondUser, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBookerAndStartIsAfter_whenFirstUser_thenReturnListBookings() {
        final int size = 3;
        List<Booking> bookings = bookingRepository.findAllByBookerAndStartIsAfter(firstUser, LocalDateTime.now(), pageable).getContent();
        assertThat(bookings.size() == size).isTrue();
        assertThat(bookings.contains(firstBooking)).isTrue();
        assertThat(bookings.contains(secondBooking)).isTrue();
        assertThat(bookings.contains(thirdBooking)).isTrue();
    }

    @Test
    public void findAllByBookerAndStartIsAfter_whenSecondUser_thenReturnListEmty() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndStartIsAfter(secondUser, LocalDateTime.now(), pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByBookerAndEndIsBefore_whenFirstUser_thenReturnListBookings() {
        final int size = 1;
        List<Booking> bookings = bookingRepository.findAllByBookerAndEndIsBefore(oldUser, LocalDateTime.now(), pageable).getContent();
        assertThat(bookings.size() == size).isTrue();
        assertThat(bookings.contains(oldBooking)).isTrue();
    }

    @Test
    public void findAllByBookerAndEndIsBefore_whenSecondUser_thenReturnListEmty() {
        List<Booking> bookings = bookingRepository.findAllByBookerAndEndIsBefore(secondUser, LocalDateTime.now(), pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByItemIdAndStatus_whenFirstUser_thenReturnListBookings() {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(itemId, StatusBooking.WAITING);
        assertThat(bookings.contains(firstBooking));
    }

    @Test
    public void findAllByItemIdAndStatus_whenBadItemId_thenReturnListBookings() {
        Long itemId = 99L;
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(itemId, StatusBooking.WAITING);
        assertThat(bookings.isEmpty());
    }

    @Test
    public void findByBookerAndItem_whenBadItemId_thenReturnEmptyBooking() {
        final int limit = 1;
        Optional<Booking> booking = bookingRepository.findByBookerAndItem(itemId, bookingId, LocalDateTime.now(), StatusBooking.WAITING.name(), limit);
        assertThat(booking.isEmpty()).isTrue();
    }

    @Test
    public void findByBookerAndItem_whenBadItemId_thenReturnBooking() {
        final int limit = 1;
        Optional<Booking> booking = bookingRepository.findByBookerAndItem(itemId, bookingId, LocalDateTime.parse("2028-10-23T17:19:33"),
                StatusBooking.REJECTED.name(), limit);
        assertThat(booking.get().equals(firstBooking));
    }

    @Test
    public void findAllByOwner_whenOwnerId_thenReturnListBookings() {
        List<Booking> bookings = bookingRepository.findAllByOwner(userId2, pageable);
        final int size = 5;
        assertThat(bookings.size() == size).isTrue();
        assertThat(bookings.contains(firstBooking)).isTrue();
        assertThat(bookings.contains(secondBooking)).isTrue();
        assertThat(bookings.contains(thirdBooking)).isTrue();
        assertThat(bookings.contains(fourthBooking)).isTrue();
        assertThat(bookings.contains(oldBooking)).isTrue();
    }

    @Test
    public void findAllByOwner_whenBadOwnerId_thenReturnListEmpty() {
        Long userId2 = 99L;
        List<Booking> bookings = bookingRepository.findAllByOwner(userId2, pageable);
        assertThat(bookings.isEmpty()).isTrue();
    }

    @Test
    public void findAllByItemIdInAndStatus_whenIds_thenReturnListBooking() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatus(Arrays.asList(1L, 2L), StatusBooking.WAITING, pageable).getContent();
        assertThat(bookings.contains(firstBooking)).isTrue();
    }

    @Test
    public void findAllByItemIdInAndStatus_whenBadIds_thenReturnListEmpty() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatus(Arrays.asList(77L, 99L), StatusBooking.WAITING, pageable).getContent();
        assertThat(bookings.isEmpty()).isTrue();
    }
}

package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

@DataJpaTest
public class UserRepositoryTest {
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

}

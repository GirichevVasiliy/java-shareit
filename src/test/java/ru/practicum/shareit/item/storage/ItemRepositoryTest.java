package ru.practicum.shareit.item.storage;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ItemRepositoryTest {
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
    final Pageable pageable = PageRequest.of(0, 10);
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
    public void findByOwnerId_whenUserId2_thenReturnItemsTest() {
        List<Item> items = itemRepository.findByOwnerId(userId2);
        assertThat(items.contains(firstItem)).isTrue();
        assertThat(items.contains(secondItem)).isTrue();
    }

    @Test
    public void findByOwnerId_whenBadUserId_thenReturnListEmptyTest() {
        final Long id = 99L;
        List<Item> items = itemRepository.findByOwnerId(id);
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void findByOwnerId_whenBadUserIdNull_thenReturnListEmptyTest() {
        List<Item> items = itemRepository.findByOwnerId(null);
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void findByOwnerIdOrderById_whenUserId2_thenReturnItemsTest() {
        List<Item> items = itemRepository.findByOwnerIdOrderById(userId2, pageable).getContent();
        assertThat(items.contains(firstItem)).isTrue();
        assertThat(items.contains(secondItem)).isTrue();
    }

    @Test
    public void findByOwnerIdOrderById_whenUserNull_thenReturnItemsTest() {
        List<Item> items = itemRepository.findByOwnerIdOrderById(null, pageable).getContent();
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    public void findByOwnerIdOrderById_whenPageableNull_thenReturnItemsTest() {
        List<Item> items = itemRepository.findByOwnerIdOrderById(userId2, null).getContent();
        assertThat(items.contains(firstItem)).isTrue();
        assertThat(items.contains(secondItem)).isTrue();
    }

    @Test
    public void findByOwnerIdOrderById_whenUserNullAndPageableNull_thenReturnItemsTest() {
        List<Item> items = itemRepository.findByOwnerIdOrderById(null, null).getContent();
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void findByOwnerIdOrderById_whenBadUserId_thenReturnListEmptyTest() {
        final Long id = 99L;
        List<Item> items = itemRepository.findByOwnerIdOrderById(id, pageable).getContent();
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void getAvailableItems_whenContainsText_thenReturnItemsTest() {
        List<Item> items = itemRepository.getAvailableItems("tex", pageable).getContent();
        assertThat(items.contains(firstItem)).isTrue();
        assertThat(items.contains(secondItem)).isTrue();
    }

    @Test
    public void getAvailableItems_whenBadNotText_thenReturnListEmptyTest() {
        List<Item> items = itemRepository.getAvailableItems("wwww", pageable).getContent();
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void findAllByRequestIn_whenContainsReques_thenReturnItemsTest() {
        List<Item> items = itemRepository.findAllByRequestIn(Arrays.asList(firstItemRequest, secondItemRequest));
        assertThat(items.contains(firstItem)).isTrue();
        assertThat(items.contains(secondItem)).isTrue();
    }

    @Test
    public void findAllByRequestIn_whenBadNotRequests_thenReturnListEmptyTest() {
        List<Item> items = itemRepository.findAllByRequestIn(new ArrayList<>());
        assertThat(items.isEmpty()).isTrue();
    }

    @Test
    public void findAllByRequest_whenContainsFirstItemRequest_thenReturnItemsTest() {
        List<Item> items = itemRepository.findAllByRequest(firstItemRequest);
        assertThat(items.contains(firstItem));
    }

    @Test
    public void findAllByRequest_whenContainsRequestNull_thenReturnListEmptyTest() {
        List<Item> items = itemRepository.findAllByRequest(null);
        assertThat(items.isEmpty()).isTrue();
    }
}

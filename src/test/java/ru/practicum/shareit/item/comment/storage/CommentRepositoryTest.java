package ru.practicum.shareit.item.comment.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CommentRepository commentRepository;
    private User firstUser;
    private ItemRequest firstItemRequest;
    private Item firstItem;
    private User onwer;
    private User secondUser;
    private Comment comment;

    @BeforeEach
    private void init() {
        firstUser = userRepository.save(new User(1L, "user1", "y1@email.ru"));
        entityManager.persist(firstUser);

        onwer = userRepository.save(new User(2L, "user2", "y2@email.ru"));
        entityManager.persist(onwer);

        secondUser = userRepository.save(new User(3L, "user3", "y3@email.ru"));
        entityManager.persist(secondUser);

        firstItemRequest = itemRequestRepository.save(new ItemRequest(1L, "text", firstUser,
                LocalDateTime.parse("2024-10-23T17:19:33")));
        entityManager.persist(firstItemRequest);

        firstItem = itemRepository.save(new Item(1L, "item1", "text", true, onwer, firstItemRequest));
        entityManager.persist(firstItem);

        comment = commentRepository.save(new Comment(1L, "comment", firstItem, secondUser, LocalDateTime.now()));
        entityManager.persist(comment);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    private void afterTest() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void addCommentTest() {
        Comment comment2 = new Comment(2L, "comment2", firstItem, secondUser, LocalDateTime.now());
        Comment newComment = commentRepository.save(comment2);
        assertThat(newComment.equals(comment2)).isTrue();
    }
}

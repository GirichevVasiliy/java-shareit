package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    private User firstUser;
    final Long userId1 = 1L;
    private User thirdUser;
    private User secondUser;
    final Pageable pageable = PageRequest.of(0, 10, Sort.by("start").descending());
    final int size = 3;
    private Page<Booking> page = new PageImpl<>(new ArrayList<>(), pageable, size);

    @BeforeEach
    private void init() {
        firstUser = userRepository.save(new User(1L, "user1", "y1@email.ru"));
        entityManager.persist(firstUser);

        secondUser = userRepository.save(new User(2L, "user2", "y2@email.ru"));
        entityManager.persist(secondUser);

        thirdUser = userRepository.save(new User(3L, "user3", "y3@email.ru"));
        entityManager.persist(thirdUser);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    private void afterTest() {
        userRepository.deleteAll();
    }

    @Test
    public void addUser_whenValidUser_thenReturnUser() {
        User saveUser = userRepository.save(firstUser);
        assertThat(saveUser.equals(firstUser)).isTrue();
    }

    @Test
    public void addUser_whenUserNull_thenThrowException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.save(null));
    }

    @Test
    public void updateUser_whenNewNameUser_thenReturnUser() {
        firstUser.setName("newName");
        User updateUser = userRepository.save(firstUser);
        assertThat(updateUser.getName().equals("newName"));
    }

    @Test
    public void updateUser_whenNewEmailUser_thenReturnUser() {
        firstUser.setEmail("newEmail@ya.ru");
        User updateUser = userRepository.save(firstUser);
        assertThat(updateUser.getEmail().equals("newEmail@ya.ru"));
    }

    @Test
    public void getUserById_whenValidUserId_thenReturnUser() {
        Optional<User> findUser = userRepository.findById(userId1);
        assertThat(findUser.get().equals(firstUser)).isTrue();
    }

    @Test
    public void getUserById_whenValidUserId_thenReturnEmpty() {
        final Long userId = 99L;
        Optional<User> findUser = userRepository.findById(userId);
        assertThat(findUser.isEmpty()).isTrue();
    }

    @Test
    public void deleteUserByIdTest_whenUserContainsDataBase() {
        Optional<User> findUser = userRepository.findById(userId1);
        assertThat(findUser.get().equals(firstUser)).isTrue();
        userRepository.deleteById(userId1);
        Optional<User> newFindUser = userRepository.findById(userId1);
        assertThat(newFindUser.isEmpty()).isTrue();
    }

    @Test
    public void deleteUserByIdTest_whenUserNotContainsDataBase_thenThrowException() {
        final Long userId = 99L;
        assertThrows(EmptyResultDataAccessException.class, () -> userRepository.deleteById(userId));
    }

    @Test
    public void getAllUsers_whenUsersContainsDataBase_thenListUsers() {
        List<User> userList = userRepository.findAll();
        assertThat(userList.contains(firstUser)).isTrue();
        assertThat(userList.contains(secondUser)).isTrue();
        assertThat(userList.contains(thirdUser)).isTrue();
    }
}

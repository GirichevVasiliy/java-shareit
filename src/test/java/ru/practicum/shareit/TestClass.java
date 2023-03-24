package ru.practicum.shareit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ValidationDateBookingException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
public class TestClass {
    @Autowired
    BookingController bookingController;
    @Autowired
    BookingService bookingService;
    @Autowired
    BookingServiceImpl bookingServiceImpl;

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ErrorHandler errorHandler;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemController itemController;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemServiceImpl itemServiceImpl;
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestController itemRequestController;

    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    ItemRequestServiceImpl itemRequestServiceImpl;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserController userController;
    @Autowired
    UserService userService;

    @Autowired
    UserService userServiceImpl;

    @Autowired
    UserRepository userRepository;


    @Test
    @SneakyThrows
    public void Test() {
        try {
            bookingController.addBooking(1L, new InputBookingDto(1L, LocalDateTime.of(2024, 01, 11, 11, 22), LocalDateTime.of(2033, 01, 11, 11, 22)));
            bookingService.getBookingById(1L, 1L);
            bookingServiceImpl.getBookingById(1L, 1L);
            bookingRepository.save(new Booking(1L, LocalDateTime.of(2023, 01, 11, 11, 22), LocalDateTime.of(2033, 01, 11, 11, 22), null, null, StatusBooking.WAITING));
            errorHandler.handleValidationDateBookingException(new ValidationDateBookingException(""));
            commentService.addComment(1L, 2L, new CommentDto(1L, "", "", LocalDateTime.of(2002, 01, 11, 11, 22)));
            commentRepository.save( new Comment(1L, "", null, null, LocalDateTime.of(2002, 01, 11, 11, 22)));
            itemController.getItemById(1L, 1L);
            itemService.getAllItems();
            itemServiceImpl.getAllItems();
            itemRepository.findAll();
            itemRequestController.getAllItemRequestsUser(1L);
            itemRequestService.getAllItemRequestsUser(1L);
            itemRequestServiceImpl.getAllItemRequestsUser(1L);
            itemRequestRepository.findById(1L);
            userController.getUsers();
            userService.getAllUsers();
            userServiceImpl.getUserById(1L);
            userRepository.findById(1L);
        } catch (Exception e){
            e.getMessage();
        }


    }

}
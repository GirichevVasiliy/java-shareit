package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestServiceImpl itemRequestService;
    private ItemRequestDto itemRequestDto;
    private AnswerDto answerDto;
    final Long userId = 1L;
    final Long onwerId = 2L;
    final Long itemId = 1L;
    final Pageable pageable = PageRequest.of(0, 2);
    @BeforeEach
    private void init() {
        answerDto = AnswerDto.builder()
                .id(1L)
                .name("answer")
                .description("answer text")
                .available(true)
                .requestId(onwerId)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("req1")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .items(Arrays.asList(answerDto))
                .build();
    }
    @Test
    void addItemRequest() {

    }

    @Test
    void getAllItemRequestsUser() {
    }

    @Test
    void getAllItemRequests() {
    }

    @Test
    void getItemRequestById() {
    }
}
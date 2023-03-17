package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.StateBooking;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestServiceImpl itemRequestService;
    @MockBean
    private UserRepository userRepository;
    private ItemRequestDto itemRequestDto;
    private AnswerDto answerDto;
    private User user;
    final Long userId = 1L;
    final Long onwerId = 2L;
    final Long itemId = 1L;
    final Long requestId = 2L;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("created").descending());

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
                .build();
        user = User.builder()
                .id(3L)
                .name("user")
                .email("ya@ya.ru")
                .build();
    }

    @Test
    @SneakyThrows
    void addItemRequestTest_whenItemRequestDtoValid_thenReturnOk() {
        when(itemRequestService.addItemRequest(any(), any())).thenReturn(itemRequestDto);
        String result = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).addItemRequest(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @Test
    @SneakyThrows
    void addItemRequestTest_whenItemRequestDtoNotValid__thenClientError() {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).addItemRequest(any(), any());
    }

    @Test
    @SneakyThrows
    void addItemRequestTest_whenUserIdNotValid__thenClientError() {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).addItemRequest(any(), any());
    }

    @Test
    @SneakyThrows
    void addItemRequestTest_whenItemRequestDtoDescreptionNotValid_thenClientError() {
        ItemRequestDto request = ItemRequestDto.builder()
                .id(1L)
                .description(null)
                .build();
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).addItemRequest(any(), any());
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsUserTest_whenUserIdValid_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getAllItemRequestsUser(userId)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getAllItemRequestsUser(any());
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsUserTest_whenEmptyListItemRequestDto_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = new ArrayList<>();
        when(itemRequestService.getAllItemRequestsUser(userId)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getAllItemRequestsUser(any());
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsUserTest_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/requests"))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).getAllItemRequestsUser(any());
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsTest_whenItemRequestDtoValid_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getAllItemRequests(userId, pageable)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getAllItemRequests(userId, pageable);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAllItemRequestsTest_whenFromDefaultValue_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getAllItemRequests(userId, pageable)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getAllItemRequests(userId, pageable);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAllItemRequestsTest_whenSizeDefaultValue_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        final Pageable pageableSize = PageRequest.of(0, 10, Sort.by("created").descending());
        when(itemRequestService.getAllItemRequests(userId, pageableSize)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getAllItemRequests(userId, pageableSize);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAllItemRequestsTest_whenSizeAndFromDefaultValue_thenReturnOk() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        final Pageable pageableSize = PageRequest.of(0, 10, Sort.by("created").descending());
        when(itemRequestService.getAllItemRequests(userId, pageableSize)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getAllItemRequests(userId, pageableSize);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAllItemRequestsTest_whenItemRequestDtoValid_thenClientError() {
      mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).getAllItemRequests(any(), any());
    }
    @Test
    @SneakyThrows
    void getItemRequestByIdTest_whenItemRequestValid_thenReturnOk() {
            when(itemRequestService.getItemRequestById(any(), any())).thenReturn(itemRequestDto);
            String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                            .header("X-Sharer-User-Id", userId))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            verify(itemRequestService, times(1)).getItemRequestById(any(), any());
            assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }
    @Test
    @SneakyThrows
    void getItemRequestByIdTest_whenRequestIdNotValid_thenClientError() {
        mockMvc.perform(get("/requests/{requestId}", "q")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).getItemRequestById(any(), any());
    }
    @Test
    @SneakyThrows
    void getItemRequestByIdTest_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/requests/{requestId}", requestId))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).getItemRequestById(any(), any());
    }
}
package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private CommentService commentService;
    private ItemDto itemDto;
    private ItemDto itemDtoUpdate;
    private UserDto ownerDto;
    private UserDto userDto;
    private CommentDto commentDto;

    final Long userId = 1L;
    final Long itemId = 1L;
    final Pageable pageable = PageRequest.of(0, 2);

    @BeforeEach
    private void init() {
        ownerDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();

        userDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();

        itemDto = ItemDto.builder()
                .name("item1")
                .description("item1Desc")
                .available(true)
                .build();
        itemDtoUpdate = ItemDto.builder()
                .name("itemNew")
                .description("item1DescNew")
                .available(false)
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .authorName("I am")
                .text("Comment")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
    }


    @Test
    @SneakyThrows
    void addItemTest_whenValidItem_thenReturnOk() {
        when(userService.getUserById(userId)).thenReturn(ownerDto);
        when(itemService.addItem(itemDto, ownerDto)).thenReturn(itemDto);
        String result = mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidItem_thenClientError() {
        ItemDto itemDto2 = ItemDto.builder()
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidUserId_thenClientError() {
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidUserIdAndItem_thenClientError() {
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidNameItem_thenClientError() {
        itemDto = ItemDto.builder()
                .name(null)
                .description("item1Desc")
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidDescriptionItem_thenClientError() {
        itemDto = ItemDto.builder()
                .name("item1")
                .description(null)
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }
    @Test
    @SneakyThrows
    void addItemTest_whenNotValidAvailableItem_thenClientError() {
        itemDto = ItemDto.builder()
                .name("item1")
                .description("description")
                .available(null)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).addItem(any(), any());
    }

    @Test
    @SneakyThrows
    void updateItemTest_whenValidItem_thenReturnOk() {
        when(itemService.updateItem(itemId, itemDto, userId)).thenReturn(itemDto);
        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }
    @Test
    @SneakyThrows
    void updateItemTest_whenNotValidItem_thenClientError() {
        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
      verify(itemService, never()).updateItem(any(), any(), any());
    }
    @Test
    @SneakyThrows
    void updateItemTest_whenNotUserId_thenClientError() {
        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).updateItem(any(), any(), any());
    }
    @Test
    @SneakyThrows
    void updateItemTest_whenNotValidItemId_thenClientError() {
        mockMvc.perform(patch("/items/{itemId}", "")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).updateItem(any(), any(), any());
    }

    @Test
    @SneakyThrows
    void getItemByIdTest_whenItemValid_thenReturnOk() {
        when(itemService.getItemById(itemId, userId)).thenReturn(itemDto);
        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getItemById(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }
    @Test
    @SneakyThrows
    void getItemByIdTest_whenItemIdNotValid_thenClientError() {
      mockMvc.perform(get("/items/{itemId}", "o")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).getItemById(any(), any());
    }
    @Test
    @SneakyThrows
    void getItemByIdTest_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).getItemById(any(), any());
    }
    @Test
    @SneakyThrows
    void getItemByIdTest_whenItemIdAndUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/items/{itemId}", ""))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).getItemById(any(), any());
    }

    @Test
    @SneakyThrows
    void getItemsByUserTest_whenDataValid_thenReturnOk() {
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getItemsByUser(userId, pageable)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getItemsByUser(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }
    @Test
    @SneakyThrows
    void getItemsByUserTest_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).getItemsByUser(any(), any());
    }
    @Test
    @SneakyThrows
    void getItemsByUserTest_whenFromDefaultValue_thenReturnOk() {
        final Pageable pageableFrom = PageRequest.of(0, 2);
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getItemsByUser(userId, pageableFrom)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getItemsByUser(userId, pageableFrom);
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }
    @Test
    @SneakyThrows
    void getItemsByUserTest_whenSizeDefaultValue_thenReturnOk() {
        final Pageable pageableFrom = PageRequest.of(0, 10);
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getItemsByUser(userId, pageableFrom)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items")
                        .param("from", "0")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getItemsByUser(userId, pageableFrom);
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAvailableItemsTest_whenValidData_thenReturnOk() {
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getAvailableItems(userId, "text", pageable)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "2")
                        .param("text", "text")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getAvailableItems(any(), any(), any());
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAvailableItemsTest_whenSizeDefaultValue_thenReturnOk() {
        final Pageable pageableFrom = PageRequest.of(0, 10);
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getAvailableItems(userId, "text", pageableFrom)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("text", "text")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getAvailableItems(userId, "text", pageableFrom);
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }
    @Test
    @SneakyThrows
    void getAvailableItemsTest_whenFromDefaultValue_thenReturnOk() {
        List<ItemDto> itemDtoList = Arrays.asList(itemDto);
        when(itemService.getAvailableItems(userId, "text", pageable)).thenReturn(itemDtoList);
        String result = mockMvc.perform(get("/items/search")
                        .param("size", "2")
                        .param("text", "text")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getAvailableItems(userId, "text", pageable);
        assertEquals(objectMapper.writeValueAsString(itemDtoList), result);
    }

    @Test
    void postComment() {
    }
}
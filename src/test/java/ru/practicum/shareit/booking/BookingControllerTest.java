package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;


    private BookingDto bookingDto;
    private ItemDto itemDto;
    private UserDto ownerDto;
    private UserDto bookerDto;
    private InputBookingDto inputBookingDto;
    final Long userId = 1L;
    final Long bookingId = 1L;
    @BeforeEach
    private void init() {
        ownerDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();

        bookerDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .owner(ownerDto)
                .requestId(2L)
                .build();
        inputBookingDto = InputBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.parse("2024-10-23T17:19:33"))
                .end(LocalDateTime.parse("2024-10-23T17:19:45"))
                .item(itemDto)
                .booker(bookerDto)
                .status("APPROVED")
                .build();
    }

    @Test
    @SneakyThrows
    void addBookingTest_whenBookingValid_thenReturnOk() {
        when(bookingService.addBooking(inputBookingDto, userId)).thenReturn(bookingDto);
        String result = mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).addBooking(any(), anyLong());
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
    @Test
    @SneakyThrows
    void addBookingTest_whenBookingNotValidEndTime_thenClientError() {
        inputBookingDto.setEnd(null);
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).addBooking(any(), anyLong());
    }
    @Test
    @SneakyThrows
    void addBookingTest_whenBookingNotValidStartTime_thenClientError() {
        inputBookingDto.setStart(null);
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).addBooking(any(), anyLong());
    }
    @Test
    @SneakyThrows
    void addBookingTest_whenBookingNotValidItemId_thenClientError() {
        inputBookingDto.setItemId(null);
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).addBooking(any(), anyLong());
    }
    @Test
    @SneakyThrows
    void updateApproveTest_whenBookingValid_thenReturnOk() {
        when(bookingService.updateApprove(bookingId, true, userId)).thenReturn(bookingDto);
        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).updateApprove(anyLong(), anyBoolean(), anyLong());
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
    @Test
    @SneakyThrows
    void updateApproveTest_whenApproveNotValid_thenReturnedClientError() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService,never()).updateApprove(anyLong(), anyBoolean(), anyLong());

    }
    @Test
    @SneakyThrows
    void updateApproveTest_whenNotUserId_thenReturnedClientError() {
       mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
        verify(bookingService,never()).updateApprove(anyLong(), anyBoolean(), anyLong());
    }
    @Test
    @SneakyThrows
    void updateApproveTest_whenNotBookingId_thenReturnedClientError() {
        assertThrows(IllegalArgumentException.class, ()->
        mockMvc.perform(patch("/bookings/{bookingId}", null)
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError()));
    }
    @Test
    void getBookingByIdTest_whenBookingValid_thenReturnOk() {
    }

    @Test
    void getAllBookings() {
    }

    @Test
    void getAllBookingsForOwner() {
    }
}
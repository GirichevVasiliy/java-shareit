package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.StateBooking;
import ru.practicum.shareit.exception.ValidationStateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private InputBookingDto inputBookingDto;
    final Long userId = 1L;
    final Long bookingId = 1L;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());

    @BeforeEach
    private void init() {
        UserDto ownerDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();

        UserDto bookerDto = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();

        ItemDto itemDto = ItemDto.builder()
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
        verify(bookingService, never()).updateApprove(anyLong(), anyBoolean(), anyLong());

    }

    @Test
    @SneakyThrows
    void updateApproveTest_whenNotUserId_thenReturnedClientError() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).updateApprove(anyLong(), anyBoolean(), anyLong());
    }

    @Test
    @SneakyThrows
    void updateApproveTest_whenNotBookingId_thenReturnedClientError() {
        mockMvc.perform(patch("/bookings/{bookingId}", "")
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest_whenBookingValid_thenReturnOk() {
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingDto);
        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getBookingById(anyLong(), anyLong());
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest_whenUserIdNotValid_thenReturnedClientError() {
        mockMvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getBookingById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest_whenBookingIdNotValid_thenReturnedClientError() {
        mockMvc.perform(get("/bookings/{bookingId}", "e")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getBookingById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest_whenBookingIdNotAndUserIdValid_thenReturnedClientError() {
        mockMvc.perform(get("/bookings/{bookingId}", ""))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getBookingById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusALL_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.ALL, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.ALL, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusPAST_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.PAST, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.PAST, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusWAITING_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.WAITING, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.WAITING, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusCURRENT_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.CURRENT, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.CURRENT, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusFUTURE_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.FUTURE, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.FUTURE, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusREJECTED_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.REJECTED, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.REJECTED, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListStatusUNSUPPORTED_STATUS_thenReturnOk() {
        when(bookingService.getAllBookings(userId, StateBooking.UNSUPPORTED_STATUS, pageable)).thenThrow(new ValidationStateException("Unknown state: UNSUPPORTED_STATUS"));
        mockMvc.perform(get("/bookings")
                        .param("state", "UNSUPPORTED_STATUS")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.UNSUPPORTED_STATUS, pageable);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListNotUserIdValid_thenReturnedClientError() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.ALL, pageable)).thenReturn(bookingDtoList);
        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", ""))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getAllBookings(userId, StateBooking.ALL, pageable);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListDefaultValueState_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.ALL, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.ALL, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListDefaultValueFrom_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        when(bookingService.getAllBookings(userId, StateBooking.ALL, pageable)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.ALL, pageable);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest_whenBookingListDefaultValueSize_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 10, Sort.by("start").descending());
        when(bookingService.getAllBookings(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookings(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusALL_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusWAITING_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.WAITING, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.WAITING, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusREJECTED_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.REJECTED, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.REJECTED, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusPAST_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.PAST, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.PAST, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusFUTURE_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.FUTURE, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.FUTURE, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusCURRENT_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.CURRENT, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.CURRENT, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListStatusUNSUPPORTED_STATUS_thenReturnedClientError() {
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.UNSUPPORTED_STATUS, pageableSize))
                .thenThrow(new ValidationStateException("Unknown state: UNSUPPORTED_STATUS"));
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "UNSUPPORTED_STATUS")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.UNSUPPORTED_STATUS, pageableSize);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingNotState_thenReturnedClientError() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingListUserIdNotValid_thenReturnedClientError() {
        final Pageable pageableSize = PageRequest.of(0, 2);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingLisFromDefaultValue_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingLisSizeDefaultValue_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 10);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwnerTest_whenBookingLisSizeAndFromAndStateDefaultValue_thenReturnOk() {
        final List<BookingDto> bookingDtoList = Arrays.asList(bookingDto);
        final Pageable pageableSize = PageRequest.of(0, 10);
        when(bookingService.getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize)).thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBookingsForOwner(userId, StateBooking.ALL, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }
}
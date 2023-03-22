package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    final Long userId = 1L;
    private UserDto userDto;
    private UserDto userDtoName;
    private UserDto userDtoEmail;

    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        userDtoName = UserDto.builder()
                .id(1L)
                .name(null)
                .email("y1@email.ru")
                .build();
        userDtoEmail = UserDto.builder()
                .id(1L)
                .name("user1")
                .email(null)
                .build();
    }

    @Test
    @SneakyThrows
    void addUserTest_whenValidUserDto_thenReturnOk() {
        when(userService.addUser(userDto)).thenReturn(userDto);
        String result = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).addUser(any());
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void addUserTest_whenUserDtoNameNotValid_thenClientError() {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).addUser(any());
    }

    @Test
    @SneakyThrows
    void addUserTest_whenUserDtoEmailNotValid_thenClientError() {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).addUser(any());
    }

    @Test
    @SneakyThrows
    void addUserTest_whenUserDtoNotValid_thenClientError() {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).addUser(any());
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserValid_thenReturnedOk() {
        when(userService.updateUser(userDto, userId)).thenReturn(userDto);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).updateUser(userDto, userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserDtoEmailEmpty_thenReturnedOk() {
        when(userService.updateUser(userDtoEmail, userId)).thenReturn(userDto);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDtoEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).updateUser(userDtoEmail, userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserDtoNameEmpty_thenReturnedOk() {
        when(userService.updateUser(userDtoName, userId)).thenReturn(userDto);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDtoName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).updateUser(userDtoName, userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserDtoNotValid_thenClientError() {
        mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).updateUser(any(), any());
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(patch("/users/{id}", "")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).updateUser(any(), any());
    }

    @Test
    @SneakyThrows
    void updateUserTest_whenUserNotFound_thenClientError() {
        when(userService.getUserById(userId)).thenThrow(
                new ResourceNotFoundException("Пользователь с ID " + userId + " не найден"));
        mockMvc.perform(patch("/users/{id}", userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void getUsers_whenUserDtoNameIsNotEmpty_thenReturnedOk() {
        List<UserDto> userDtoList = Arrays.asList(userDto);
        when(userService.getAllUsers()).thenReturn(userDtoList);
        String result = mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(userDtoList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).getAllUsers();
        assertEquals(objectMapper.writeValueAsString(userDtoList), result);
    }

    @Test
    @SneakyThrows
    void getUsers_whenUserDtoListIsEmpty_thenReturnedOk() {
        List<UserDto> userDtoList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(userDtoList);
        String result = mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(userDtoList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).getAllUsers();
        assertEquals(objectMapper.writeValueAsString(userDtoList), result);
    }

    @Test
    @SneakyThrows
    void getUserById_whenUserIdValid_thenReturnedOk() {
        when(userService.getUserById(userId)).thenReturn(userDto);
        String result = mockMvc.perform(get("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).getUserById(userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void getUserById_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(get("/users/{id}", "q"))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).getUserById(userId);
    }

    @Test
    @SneakyThrows
    void getUserById_whenUserNotFound_thenClientError() {
        when(userService.getUserById(userId)).thenThrow(
                new ResourceNotFoundException("Пользователь с ID " + userId + " не найден"));
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void deleteUser_whenUserNotFound_thenClientError() {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void deleteUser_whenUserIdNotValid_thenClientError() {
        mockMvc.perform(delete("/users/{id}", "e"))
                .andExpect(status().is4xxClientError());
    }
}
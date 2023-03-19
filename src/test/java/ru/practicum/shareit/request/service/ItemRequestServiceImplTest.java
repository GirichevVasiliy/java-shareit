package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private User user;
    final Long userId1 = 1L;
    final Long requestId = 1L;
    private ItemRequest itemRequest;
    final Pageable pageable = PageRequest.of(0, 2, Sort.by("start").descending());
    final int size = 0;
    private ItemRequestDto itemRequestDto;
    private Page<ItemRequest> pageItemRequest = new PageImpl<>(new ArrayList<>(), pageable, size);

    @BeforeEach
    private void init() {
        user = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("text")
                .requestor(user)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("text")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
    }

    @Test
    void forAllTests_whenUserNotFound_thenThrowException() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.addItemRequest(itemRequestDto, userId1));
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void addItemRequest_whenСorrectData_thenReturnItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);
        verify(itemRequestRepository, times(1)).save(any());
        assertThat(newItemRequest.equals(itemRequest)).isTrue();
    }

    @Test
    void getAllItemRequestsUser_whenNotFoundUser_thenReturnListItemRequestDto() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getAllItemRequestsUser(userId1));
        verify(itemRequestRepository, never()).findByRequestorIdOrderByCreatedDesc(any());
    }

    @Test
    void getAllItemRequestsUser_whenСorrectData_thenReturnListItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId1)).thenReturn(new ArrayList<ItemRequest>());
        when(itemRepository.findAllByRequestIn(Arrays.asList(itemRequest))).thenReturn(new ArrayList<Item>());
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllItemRequestsUser(userId1);
        assertThat(requestDtoList.isEmpty()).isTrue();
    }

    @Test
    void getAllItemRequests_whenСorrectData_thenReturnListItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestorNot(user, pageable)).thenReturn(pageItemRequest);
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllItemRequests(userId1, pageable);
        assertThat(requestDtoList.isEmpty()).isTrue();
    }

    @Test
    void getAllItemRequests_whenNotFoundUser_thenReturnListItemRequestDto() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getAllItemRequests(userId1, pageable));
        verify(itemRequestRepository, never()).findAllByRequestorNot(any(), any());
    }

    @Test
    void getItemRequestById_whenСorrectData_thenReturnListItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest(itemRequest)).thenReturn(new ArrayList<>());
        ItemRequestDto itemRequestDtoNew = itemRequestService.getItemRequestById(requestId, userId1);
        assertThat(itemRequestDtoNew.getId().equals(itemRequestDto.getId())).isTrue();
        assertThat(itemRequestDtoNew.getDescription().equals(itemRequestDto.getDescription())).isTrue();
        assertThat(itemRequestDtoNew.getCreated().equals(itemRequestDto.getCreated())).isTrue();
    }

    @Test
    void getItemRequestById_whenNotFoundItemRequest_thenReturnListItemRequestDto() {
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user));
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getItemRequestById(requestId, userId1));
        verify(itemRepository, never()).findAllByRequest(any());
    }

    @Test
    void getItemRequestById_whenNotFoundUser_thenReturnListItemRequestDto() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> itemRequestService.getItemRequestById(requestId, userId1));
        verify(itemRepository, never()).findAllByRequest(any());
    }
}
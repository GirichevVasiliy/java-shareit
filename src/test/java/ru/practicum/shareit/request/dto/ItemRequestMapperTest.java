package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ItemRequestMapperTest {
    private Answer answer;
    private User owner;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private AnswerDto answerDto;

    @BeforeEach
    private void init() {
        owner = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("text")
                .requestor(owner)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .build();
        answer = Answer.builder()
                .itemId(1L)
                .itemName("name")
                .available(true)
                .description("description")
                .requestId(1L)
                .build();
        answerDto = AnswerDto.builder()
                .id(1L)
                .name("name")
                .available(true)
                .description("description")
                .requestId(1L)
                .build();
    }

    @Test
    @DisplayName("Тест создания ItemRequestDto в ItemRequestDto")
    void itemRequestDtoCreateTest() {
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestDtoCreate(itemRequestDto);
        assertThat(newItemRequestDto.getId() == null).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequestDto.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated() != null).isTrue();
    }

    @Test
    @DisplayName("Тест создания ItemRequestDto в ItemRequestDto, Description = null")
    void itemRequestDtoCreateDescriptionIsNullTest() {
        itemRequestDto = ItemRequestDto.builder()
                .description(null)
                .build();
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestDtoCreate(itemRequestDto);
        assertThat(newItemRequestDto.getId() == null).isTrue();
        assertThat(newItemRequestDto.getDescription() == null).isTrue();
        assertThat(newItemRequestDto.getCreated() != null).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest, answer в ItemRequestDto")
    void itemRequestAndListAnswersToDtoTest() {
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestAndListAnswersToDto(itemRequest, Arrays.asList(answer));
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().equals(Arrays.asList(answerDto))).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest, answer в ItemRequestDto")
    void itemRequestAndListAnswersToDtoIdIsNullTest() {
        itemRequest.setId(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestAndListAnswersToDto(itemRequest, Arrays.asList(answer));
        assertThat(newItemRequestDto.getId() == null).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().equals(Arrays.asList(answerDto))).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest, answer в ItemRequestDto")
    void itemRequestAndListAnswersToDtoDescriptionIsNullTest() {
        itemRequest.setDescription(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestAndListAnswersToDto(itemRequest, Arrays.asList(answer));
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription() == null).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().equals(Arrays.asList(answerDto))).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest, answer в ItemRequestDto")
    void itemRequestAndListAnswersToDtoCreatedIsNullTest() {
        itemRequest.setCreated(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestAndListAnswersToDto(itemRequest, Arrays.asList(answer));
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated() == null).isTrue();
        assertThat(newItemRequestDto.getItems().equals(Arrays.asList(answerDto))).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest, answer в ItemRequestDto")
    void itemRequestAndListAnswersToDtoListAnswerIsEmptyTest() {
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestAndListAnswersToDto(itemRequest, new ArrayList<>());
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().equals(new ArrayList<>())).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest ItemRequestDto, с чистым списком items")
    void itemRequestToDtoNewListIsEmptyTest() {
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestToDto(itemRequest);
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest ItemRequestDto, с чистым списком items, setId=null")
    void itemRequestToDtoNewListIsEmptyAndIdIsNullTest() {
        itemRequest.setId(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestToDto(itemRequest);
        assertThat(newItemRequestDto.getId() == null).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest ItemRequestDto, с чистым списком items, Description = null")
    void itemRequestToDtoNewListIsEmptyAndDescriptionIsNullTest() {
        itemRequest.setDescription(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestToDto(itemRequest);
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription() == null).isTrue();
        assertThat(newItemRequestDto.getCreated().equals(itemRequest.getCreated())).isTrue();
        assertThat(newItemRequestDto.getItems().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест создания itemRequest ItemRequestDto, с чистым списком items, Created = null")
    void itemRequestToDtoNewListIsEmptyAndCreatedIsNullTest() {
        itemRequest.setCreated(null);
        ItemRequestDto newItemRequestDto = ItemRequestMapper.itemRequestToDto(itemRequest);
        assertThat(newItemRequestDto.getId().equals(itemRequest.getId())).isTrue();
        assertThat(newItemRequestDto.getDescription().equals(itemRequest.getDescription())).isTrue();
        assertThat(newItemRequestDto.getCreated() == null).isTrue();
        assertThat(newItemRequestDto.getItems().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Тест создания ItemRequest из itemRequestDto, User")
    void itemRequestCreateTest() {
        itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        ItemRequest newItemRequest = ItemRequestMapper.itemRequestCreate(itemRequestDto, owner);
        assertThat(newItemRequest.getDescription().equals(itemRequestDto.getDescription())).isTrue();
        assertThat(newItemRequest.getCreated().equals(itemRequestDto.getCreated())).isTrue();
        assertThat(newItemRequest.getRequestor().equals(owner)).isTrue();
    }

    @Test
    @DisplayName("Тест создания ItemRequest из itemRequestDto, User, description=null")
    void itemRequestCreateDescriptionIsNullTest() {
        itemRequestDto = ItemRequestDto.builder()
                .description(null)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        ItemRequest newItemRequest = ItemRequestMapper.itemRequestCreate(itemRequestDto, owner);
        assertThat(newItemRequest.getDescription() == null).isTrue();
        assertThat(newItemRequest.getCreated().equals(itemRequestDto.getCreated())).isTrue();
        assertThat(newItemRequest.getRequestor().equals(owner)).isTrue();
    }

    @Test
    @DisplayName("Тест создания ItemRequest из itemRequestDto, User, Created=null")
    void itemRequestCreateCreatedIsNullTest() {
        itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .created(null)
                .build();
        ItemRequest newItemRequest = ItemRequestMapper.itemRequestCreate(itemRequestDto, owner);
        assertThat(newItemRequest.getDescription().equals(itemRequestDto.getDescription())).isTrue();
        assertThat(newItemRequest.getCreated() == null).isTrue();
        assertThat(newItemRequest.getRequestor().equals(owner)).isTrue();
    }

    @Test
    @DisplayName("Тест создания ItemRequest из itemRequestDto, User, Requestor=null")
    void itemRequestCreateRequestorIsNullTest() {
        itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        ItemRequest newItemRequest = ItemRequestMapper.itemRequestCreate(itemRequestDto, null);
        assertThat(newItemRequest.getDescription().equals(itemRequestDto.getDescription())).isTrue();
        assertThat(newItemRequest.getCreated().equals(itemRequestDto.getCreated())).isTrue();
        assertThat(newItemRequest.getRequestor() == null).isTrue();
    }
}
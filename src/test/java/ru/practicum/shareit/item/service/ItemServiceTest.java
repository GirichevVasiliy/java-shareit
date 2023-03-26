package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    @Autowired
    ItemServiceImpl itemService;
    Pageable pageable = PageRequest.of(0, 10);
    private UserDto ownerDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private CommentDto commentDto;
    private CommentDto commentDtoSecond;

    @BeforeEach
    private void init() {
        userDto = UserDto.builder()
                .id(4L)
                .name("user4")
                .email("y@4email.ru")
                .build();
        ownerDto = UserDto.builder()
                .id(5L)
                .name("user5")
                .email("y@5email.ru")
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("comment1")
                .authorName("user4")
                .created(LocalDateTime.parse("2023-05-12T18:00"))
                .build();
        commentDtoSecond = CommentDto.builder()
                .text("comment2")
                .authorName("user4")
                .created(LocalDateTime.parse("2023-05-12T10:00"))
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("item1Desc")
                .available(true)
                .requestId(1L)
                .owner(ownerDto)
                .comments(Arrays.asList(commentDto))
                .build();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetItemsByUser() {
        final Long ownerId = 5L;
        final int item1 = 0;
        final int item2 = 1;
        final int item3 = 2;
        final int item4 = 3;
        final int item5 = 4;
        final int size = 5;
        List<ItemDto> itemDtoList = itemService.getItemsByUser(ownerId, pageable);
        assertThat(itemDtoList.size() == size).isTrue();
        assertThat(itemDtoList.get(item1).getOwner().getId() == ownerId).isTrue();
        assertThat(itemDtoList.get(item2).getOwner().getId() == ownerId).isTrue();
        assertThat(itemDtoList.get(item3).getOwner().getId() == ownerId).isTrue();
        assertThat(itemDtoList.get(item4).getOwner().getId() == ownerId).isTrue();
        assertThat(itemDtoList.get(item5).getOwner().getId() == ownerId).isTrue();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetItemById() {
        final Long userId = 4L;
        final Long itemId = 1L;
        ItemDto newItemDto = itemService.getItemById(itemId, userId);
        assertThat(newItemDto.equals(itemDto)).isTrue();
    }

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestAddComment() {
        final Long userId = 4L;
        final Long itemId = 1L;
        CommentDto newCommentDto = itemService.addComment(itemId, userId, commentDtoSecond);
        assertThat(newCommentDto.getText().equals(commentDtoSecond.getText())).isTrue();
        assertThat(newCommentDto.getAuthorName().equals(commentDtoSecond.getAuthorName())).isTrue();
        assertThat(newCommentDto.getCreated().equals(commentDtoSecond.getCreated())).isTrue();
    }
}
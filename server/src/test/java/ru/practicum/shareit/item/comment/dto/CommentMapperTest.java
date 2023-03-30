package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentMapperTest {
    private User owner;
    private User firstUser;
    private ItemRequest itemRequest;
    private Item item;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    private void init() {
        owner = User.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();
        firstUser = User.builder()
                .id(2L)
                .name("user2")
                .email("y2@email.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("text")
                .requestor(owner)
                .created(LocalDateTime.parse("2024-10-23T17:19:33"))
                .build();
        item = Item.builder()
                .id(1L)
                .name("item1")
                .description("text")
                .available(true)
                .owner(owner)
                .comments(new ArrayList<>())
                .request(itemRequest)
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .item(item)
                .author(firstUser)
                .created(LocalDateTime.parse("2024-12-23T17:19:33"))
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .authorName("user2")
                .created(LocalDateTime.parse("2024-12-23T17:19:33"))
                .build();
    }

    @Test
    public void toCommentDtoTest() {
        CommentDto newCommentDto = CommentMapper.toCommentDto(comment);
        assertThat(newCommentDto.equals(commentDto)).isTrue();
    }

    @Test
    public void toCommentDtoCommentAuthorIsNullTest() {
        comment.setAuthor(null);
        assertThrows(NullPointerException.class, () -> CommentMapper.toCommentDto(comment));
    }

    @Test
    public void toCommentDtoCommentTextIsNullTest() {
        comment.setText(null);
        CommentDto newCommentDto = CommentMapper.toCommentDto(comment);
        assertThat(newCommentDto.getText() == null).isTrue();
    }

    @Test
    public void toCommentTest() {
        Comment newComment = CommentMapper.toComment(commentDto, firstUser, item);
        assertThat(newComment.equals(comment)).isTrue();
    }

    @Test
    public void toCommentTestUserIsNull() {
        Comment newComment = CommentMapper.toComment(commentDto, null, item);
        assertThat(newComment.getAuthor() == null).isTrue();
        assertThat(newComment.getId().equals(comment.getId())).isTrue();
        assertThat(newComment.getText().equals(comment.getText())).isTrue();
        assertThat(newComment.getItem().equals(comment.getItem())).isTrue();
        assertThat(newComment.getCreated().equals(comment.getCreated())).isTrue();
    }

    @Test
    public void toCommentTestItemIsNull() {
        Comment newComment = CommentMapper.toComment(commentDto, firstUser, null);
        assertThat(newComment.getAuthor().equals(comment.getAuthor())).isTrue();
        assertThat(newComment.getId().equals(comment.getId())).isTrue();
        assertThat(newComment.getText().equals(comment.getText())).isTrue();
        assertThat(newComment.getItem() == null).isTrue();
        assertThat(newComment.getCreated().equals(comment.getCreated())).isTrue();
    }

    @Test
    public void toCommentTestCommentDtoIsNull() {
        assertThrows(NullPointerException.class, () -> CommentMapper.toComment(null, firstUser, item));
    }
}

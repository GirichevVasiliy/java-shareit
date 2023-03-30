package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.CommentDto;

public interface CommentService {
    CommentDto addComment(Long itemId, Long authorId, CommentDto commentDto);
}
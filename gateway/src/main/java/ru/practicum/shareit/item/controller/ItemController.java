package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Add item {}, userId={}", itemDto, userId);
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable("itemId") Long itemId,
                                             @Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Update item {}, userId={}, itemId={}", itemDto, userId, itemId);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item by id itemId={} userId={}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get item by user id, userId={}, from={},  size={}", userId, from, size);
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAvailableItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam String text,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search item by text={}, userId={}, from={},  size={}",text, userId, from, size);
        return itemClient.getAvailableItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long authorId,
                                              @Valid @RequestBody CommentDto commentDto) {
        log.info("Add comment {}, itemId={}, authorId={}",commentDto, itemId, authorId);
        return itemClient.addComment(itemId, authorId, commentDto);
    }
}

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
import javax.validation.constraints.NotNull;
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
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable("itemId") @NotNull Long itemId,
                                             @Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") @NotNull Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getItemsByUser(userId, from, size);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> getAvailableItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                    @RequestParam String text,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getAvailableItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable @NotNull Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") @NotNull Long authorId,
                                              @Valid @RequestBody CommentDto commentDto
    ) {
        return itemClient.addComment(itemId, authorId, commentDto);
    }
}

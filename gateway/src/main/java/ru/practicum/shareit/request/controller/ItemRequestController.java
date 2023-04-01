package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Add itemRequest {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.addItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all itemRequest for user, userId={}", userId);
        return itemRequestClient.getAllItemRequestsUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all itemRequest, userId={}, from={},  size={}", userId, from, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get itemRequest by id, requestId={}, userId={}", requestId, userId);
        return itemRequestClient.getItemRequestById(requestId, userId);
    }
}

package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.util.PageableFactory;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto), userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllItemRequestsUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequests(userId, PageableFactory.getPageableSortDescCreated(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }
}

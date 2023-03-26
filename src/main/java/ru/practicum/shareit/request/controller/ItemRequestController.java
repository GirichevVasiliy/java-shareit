package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationForPageableException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto), userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.getAllItemRequestsUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return itemRequestService.getAllItemRequests(userId, getPageable(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable @NotNull Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    private Pageable getPageable(int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidationForPageableException("Неверно заданы данные для поиска");
        }
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sortByStart);
        return pageable;
    }
}

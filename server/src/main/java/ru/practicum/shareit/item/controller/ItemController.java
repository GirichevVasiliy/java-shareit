package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageableFactory;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService, CommentService commentService) {
        this.itemService = itemService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * Добавление новой вещи и юзер кто добавил, владелец
     */
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.getUserById(userId);
        return itemService.addItem(itemDto, userDto);
    }

    /**
     * Редактирование вещи только владельцем
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId") Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemId, itemDto, userId);
    }

    /**
     * Просмотр информации о конкретной вещи по её идентификатору.
     * Эндпойнт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return itemService.getItemsByUser(userId, PageableFactory.getPageable(from, size));
    }

    /**
     * *Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст,
     * и система ищет вещи, содержащие этот текст в названии или описании.
     * Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
     * Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @GetMapping("/search")
    public List<ItemDto> getAvailableItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam String text,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return itemService.getAvailableItems(userId, text, PageableFactory.getPageable(from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") @NotNull Long authorId,
                                  @Validated @RequestBody CommentDto commentDto
    ) {
        return commentService.addComment(itemId, authorId, commentDto);
    }
}

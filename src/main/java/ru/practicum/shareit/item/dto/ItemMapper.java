package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDtoSingl(Item item) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .owner(UserMapper.userToDto(item.getOwner()))
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto toItemDtoFull(Item item, List<CommentDto> comments, DateBookingDto lastBooking, DateBookingDto nextBooking) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .owner(UserMapper.userToDto(item.getOwner()))
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    public static ItemDto toItemDtoList(Item item, List<CommentDto> comments) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .owner(UserMapper.userToDto(item.getOwner()))
                .comments(comments)
                .build();
    }
    public static Answer answerCreateForItem(Item item){
        return Answer.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .ownerId(item.getOwner().getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .build();
    }
}

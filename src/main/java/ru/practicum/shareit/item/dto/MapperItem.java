package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapperItem {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
        itemDto.setId(item.getId());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
               // что то не хватает со стороны запроса itemDto.getRequestId() != null ?  :null
                null);
    }
}

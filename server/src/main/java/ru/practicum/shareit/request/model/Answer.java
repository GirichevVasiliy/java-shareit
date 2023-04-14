package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Answer {
    private final Long itemId;
    private final String itemName;
    private final Boolean available;
    private final String description;
    private final Long requestId;


}

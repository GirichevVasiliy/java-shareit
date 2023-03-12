package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Answer {
    private final Long itemId;
    private final String itemName;
    private final Long ownerId;

}

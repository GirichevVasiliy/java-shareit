package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private final Long itemId;
    private final String description;
    private final Long ownerId;
}

package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private final Long id;
    private final String name;
   // private final Long ownerId;

    private final String description;
    private final Boolean available;
    private final Long requestId;
}

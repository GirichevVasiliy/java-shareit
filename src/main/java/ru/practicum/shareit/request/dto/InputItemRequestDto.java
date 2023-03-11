package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InputItemRequestDto {
    @NotNull
    private final String description;
}

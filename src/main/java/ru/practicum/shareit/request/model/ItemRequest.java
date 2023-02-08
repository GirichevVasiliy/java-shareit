package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private final Long id;
    @NotNull
    private final String description;
    @NotNull
    private final User requestor;
    @NotNull
    private final LocalDateTime created = LocalDateTime.now();
}

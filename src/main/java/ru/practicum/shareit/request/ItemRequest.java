package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    @NotNull
    private String description;
    @NotNull
    private User requestor;
    @NotNull
    LocalDateTime created = LocalDateTime.now();
}

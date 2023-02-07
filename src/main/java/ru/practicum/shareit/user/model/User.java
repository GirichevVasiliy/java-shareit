package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Value
@Builder
public class User {
    @With
    Long id;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9.]+[^._]@[^.\\-_]+[a-zA-Z0-9.]+[a-zA-Z0-9]$", message = "Email введен некорректно")
    String email;
    @NotNull
    @NotBlank(message = "Поле Name не должно быть пустым")
    String name;
    @With
    @Builder.Default
    List<Item> itemsUser = Collections.emptyList();
}

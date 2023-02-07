package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long id;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9.]+[^._]@[^.\\-_]+[a-zA-Z0-9.]+[a-zA-Z0-9]$", message = "Email введен некорректно")
    private String email;
    @NotNull
    @NotBlank(message = "Поле Name не должно быть пустым")
    private String name;
    @Builder.Default
    private List<Item> itemsUser = Collections.emptyList();

    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}

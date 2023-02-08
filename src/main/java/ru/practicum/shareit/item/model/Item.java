package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    @NotNull
    @NotBlank(message = "Поле \"Название\" должно быть заполнено")
    private String name;
    @NotNull
    @Size(max = 200, message = "Максимальное кол-во символов для описания: 200")
    private String description;
    @BooleanFlag
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Item(String name, String description, Boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}

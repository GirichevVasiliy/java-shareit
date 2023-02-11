package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private final Long id;
    @NotBlank(message = "Поле \"Название\" должно быть заполнено")
    private final String name;
    @NotBlank(message = "Поле \"Описание\" должно быть заполнено")
    private final String description;
    @BooleanFlag
    @NotNull
    private final Boolean available; // статус о том, доступна или нет вещь для аренды;
    private final Long requestId; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.
    User owner;

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}

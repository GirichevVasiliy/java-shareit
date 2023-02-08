package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    @NotNull
    @NotBlank(message = "Поле \"Название\" должно быть заполнено")
    private String name;
    @NotNull
    @Size(max = 200, message = "Максимальное кол-во символов для описания: 200")
    private String description;
    @BooleanFlag
    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды;
    private User owner; // владелец вещи - надо скрыть
    private int counterRental;
    private Long requestId; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public ItemDto(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}

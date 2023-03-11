package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
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
    private final UserDto owner;
    private final List<CommentDto> comments;
    private final DateBookingDto lastBooking;
    private final DateBookingDto nextBooking;
}

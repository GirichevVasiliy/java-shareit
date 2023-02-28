package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class InputBookingDto {
    @NotNull
    private Long ItemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;

    public InputBookingDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        ItemId = itemId;
        this.start = start;
        this.end = end;
    }
}

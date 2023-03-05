package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class InputBookingDto {
    @NotNull
    private Long itemId;
    @NotNull
    @JsonFormat
    private LocalDateTime start;
    @NotNull
    @JsonFormat
    private LocalDateTime end;

    public InputBookingDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}

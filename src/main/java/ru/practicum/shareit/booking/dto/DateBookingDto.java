package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class DateBookingDto {
    private final Long id;
    private final Long bookerId;
    @JsonFormat
    private final LocalDateTime start;
    @JsonFormat
    private final LocalDateTime end;
}

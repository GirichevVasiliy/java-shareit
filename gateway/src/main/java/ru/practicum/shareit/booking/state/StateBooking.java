package ru.practicum.shareit.booking.state;

import java.util.Optional;

public enum StateBooking {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED, UNSUPPORTED_STATUS;

    public static Optional<StateBooking> from(String stringState) {
        for (StateBooking state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}


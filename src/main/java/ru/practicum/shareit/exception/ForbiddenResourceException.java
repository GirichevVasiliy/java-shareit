package ru.practicum.shareit.exception;

public class ForbiddenResourceException extends RuntimeException {
    public ForbiddenResourceException(String message) {
        super(message);
    }
}

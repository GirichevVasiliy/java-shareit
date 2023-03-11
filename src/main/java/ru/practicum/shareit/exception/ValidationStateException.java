package ru.practicum.shareit.exception;

public class ValidationStateException extends RuntimeException {
    public ValidationStateException(String message) {
        super(message);
    }
}

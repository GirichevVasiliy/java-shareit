package ru.practicum.shareit.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationDateException extends RuntimeException {
    public ValidationDateException(String message) {
        super(message);
    }
}
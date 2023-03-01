package ru.practicum.shareit.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationAvailableException extends RuntimeException {
    public ValidationAvailableException(String message) {
        super(message);
    }
}

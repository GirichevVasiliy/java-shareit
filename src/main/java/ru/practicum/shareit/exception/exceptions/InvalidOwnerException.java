package ru.practicum.shareit.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class InvalidOwnerException extends RuntimeException {
    public InvalidOwnerException(String message) {
        super(message);
    }
}

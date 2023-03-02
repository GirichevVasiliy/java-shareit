package ru.practicum.shareit.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ValidationOwnerException extends RuntimeException {
        public ValidationOwnerException(String message) {
        super(message);
    }
}


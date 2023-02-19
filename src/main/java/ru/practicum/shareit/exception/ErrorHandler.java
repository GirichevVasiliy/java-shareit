package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.ForbiddenResourceException;
import ru.practicum.shareit.exception.exceptions.InvalidOwnerException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(final ResourceNotFoundException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleInvalidOwnerException(final InvalidOwnerException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenResourceException(final ForbiddenResourceException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    private class ErrorResponse {
        private final String error;

        private ErrorResponse(String error) {
            this.error = error;
        }

        private String getError() {
            return error;
        }
    }
}

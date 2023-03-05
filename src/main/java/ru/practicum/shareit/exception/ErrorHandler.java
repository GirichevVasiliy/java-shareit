package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.*;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(final ResourceNotFoundException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleInvalidOwnerException(final InvalidOwnerException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenResourceException(final ForbiddenResourceException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleValidationOwnerException(final ValidationOwnerException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationDateException(final ValidationDateException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    //@ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationAvailableException(final ValidationAvailableException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
   @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationStateException(final ValidationStateException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return error;
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

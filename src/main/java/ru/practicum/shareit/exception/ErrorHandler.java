package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFoundException(final ResourceNotFoundException e) {
        return Map.of("404", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public Map<String, String> handleInvalidOwnerException(final InvalidOwnerException e) {
        return Map.of("409", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, String> handleForbiddenResourceException(final ForbiddenResourceException e) {
        return Map.of("403", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleValidationOwnerException(final ValidationOwnerException e) {
        return Map.of("404", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationDateException(final ValidationDateException e) {
        return Map.of("400", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationAvailableException(final ValidationAvailableException e) {
        return Map.of("400", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationStateException(final ValidationStateException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationDateBookingException(final ValidationDateBookingException e) {
        return Map.of("400", e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationForPageableException(final ValidationForPageableException e) {
        return Map.of("400", e.getMessage());
    }
}

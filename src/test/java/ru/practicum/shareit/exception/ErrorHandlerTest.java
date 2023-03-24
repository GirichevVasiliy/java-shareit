package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ErrorHandlerTest {
    @Autowired
    private ErrorHandler errorHandler;

    @Test
    void handleResourceNotFoundException() {
        errorHandler.handleForbiddenResourceException(new ForbiddenResourceException(""));
    }

    @Test
    void handleInvalidOwnerException() {
        errorHandler.handleInvalidOwnerException(new InvalidOwnerException(""));
    }

    @Test
    void handleForbiddenResourceException() {
        errorHandler.handleForbiddenResourceException(new ForbiddenResourceException(""));
    }

    @Test
    void handleValidationOwnerException() {
        errorHandler.handleValidationOwnerException(new ValidationOwnerException(""));
    }

    @Test
    void handleValidationDateException() {
        errorHandler.handleValidationDateException(new ValidationDateException(""));
    }

    @Test
    void handleValidationAvailableException() {
        errorHandler.handleValidationAvailableException(new ValidationAvailableException(""));
    }

    @Test
    void handleValidationStateException() {
        errorHandler.handleValidationStateException(new ValidationStateException(""));
    }

    @Test
    void handleValidationDateBookingException() {
        errorHandler.handleValidationDateBookingException(new ValidationDateBookingException(""));
    }
}
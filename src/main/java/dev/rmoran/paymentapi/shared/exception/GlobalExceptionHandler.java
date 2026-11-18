package dev.rmoran.paymentapi.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFound(CustomerNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now());
    }

    @ExceptionHandler(EmptyUpdateRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyUpdateRequestException(EmptyUpdateRequestException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), Instant.now());
    }
}

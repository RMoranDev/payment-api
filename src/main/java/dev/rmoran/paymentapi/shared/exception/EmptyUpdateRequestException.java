package dev.rmoran.paymentapi.shared.exception;

public class EmptyUpdateRequestException extends RuntimeException {
    public EmptyUpdateRequestException() {
        super("At least one field must be provided");
    }
}

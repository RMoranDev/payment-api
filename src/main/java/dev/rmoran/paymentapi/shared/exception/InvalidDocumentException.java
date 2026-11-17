package dev.rmoran.paymentapi.shared.exception;

public class InvalidDocumentException extends RuntimeException {
    public InvalidDocumentException(String message) {
        super(message);
    }
}

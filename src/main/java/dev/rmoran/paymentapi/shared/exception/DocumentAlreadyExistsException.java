package dev.rmoran.paymentapi.shared.exception;

public class DocumentAlreadyExistsException extends RuntimeException {
    public DocumentAlreadyExistsException(String document) {
        super("Document already exists: " + document);
    }
}

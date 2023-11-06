package io.events.exceptions;

public class OriginalUrlMatchException extends RuntimeException {

    public OriginalUrlMatchException() {
        super("No original URL match found.");
    }

    public OriginalUrlMatchException(String message) {
        super(message);
    }

    public OriginalUrlMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}

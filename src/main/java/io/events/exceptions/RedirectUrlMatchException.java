package io.events.exceptions;

public class RedirectUrlMatchException extends RuntimeException {

    public RedirectUrlMatchException() {
        super("No redirect URL match found.");
    }

    public RedirectUrlMatchException(String message) {
        super(message);
    }

    public RedirectUrlMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}

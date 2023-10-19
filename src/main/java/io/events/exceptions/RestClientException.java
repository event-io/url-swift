package io.events.exceptions;

public class RestClientException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RestClientException(String message) {
        super(message);
    }
    
    public RestClientException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RestClientException(Throwable cause) {
        super(cause);
    }
    
    public RestClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package org.nampython.center.requesthandler.exception;

public class CannotParseRequestException extends RuntimeException {
    public CannotParseRequestException(String message) {
        super(message);
    }

    public CannotParseRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

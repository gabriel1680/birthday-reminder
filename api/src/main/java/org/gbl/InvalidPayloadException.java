package org.gbl;

public class InvalidPayloadException extends RuntimeException {

    public InvalidPayloadException(Throwable cause) {
        super("invalid payload", cause);
    }
}

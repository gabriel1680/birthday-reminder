package org.gbl.controller.contacts;

public class InvalidPayloadException extends RuntimeException {

    public InvalidPayloadException(Throwable cause) {
        super("invalid payload", cause);
    }

    public InvalidPayloadException(String message) {
        super(message);
    }
}

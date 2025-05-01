package org.gbl.contacts.application.service.query;

public class InvalidSearchInputException extends RuntimeException {
    public InvalidSearchInputException(String message) {
        super(message);
    }
}

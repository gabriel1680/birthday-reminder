package org.gbl;

public class ContactNumberAlreadyExistsException extends RuntimeException {

    public ContactNumberAlreadyExistsException(String phoneNumber) {
        super("Contact already exists with number \"%s\"".formatted(phoneNumber));
    }
}

package org.gbl.contacts.usecase.add;

public class ContactNumberAlreadyExistsException extends RuntimeException {

    public ContactNumberAlreadyExistsException(String phoneNumber) {
        super("Contact already exists with number \"%s\"".formatted(phoneNumber));
    }
}

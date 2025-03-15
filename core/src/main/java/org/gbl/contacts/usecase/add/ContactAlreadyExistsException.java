package org.gbl.contacts.usecase.add;

public class ContactAlreadyExistsException extends RuntimeException {

    public ContactAlreadyExistsException(String name) {
        super("Contact already exists with name \"%s\"".formatted(name));
    }
}

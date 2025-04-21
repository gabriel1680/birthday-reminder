package org.gbl.contacts.application.usecase.shared;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(String aNumber) {
        super("Contact with id \"%s\" not found".formatted(aNumber));
    }
}

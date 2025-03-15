package org.gbl.contacts.usecase.shared;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(String aNumber) {
        super("Contact with id \"%s\" not found".formatted(aNumber));
    }
}

package org.gbl.contacts.usecase.remove;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(String aNumber) {
        super("Contact with number \"%s\" not found".formatted(aNumber));
    }
}

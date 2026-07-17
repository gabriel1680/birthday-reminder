package org.gbl.view;

import org.gbl.common.gateway.ContactResponse;

import java.time.format.DateTimeFormatter;

public final class ContactViewPresenter {

    private static final DateTimeFormatter BIRTHDATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ContactViewPresenter() {
    }

    public static ContactView toView(ContactResponse contact) {
        return new ContactView(
                contact.id(),
                contact.name(),
                BIRTHDATE_FORMAT.format(contact.birthdate()));
    }
}

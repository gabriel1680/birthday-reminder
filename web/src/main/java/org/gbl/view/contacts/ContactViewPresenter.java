package org.gbl.view.contacts;

import org.gbl.common.gateway.ContactResponse;

import java.time.format.DateTimeFormatter;

public final class ContactViewPresenter {

    private static final DateTimeFormatter BIRTHDATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ContactViewPresenter() {
    }

    public static ContactViewModel toView(ContactResponse contact) {
        return new ContactViewModel(
                contact.id(),
                contact.name(),
                BIRTHDATE_FORMAT.format(contact.birthdate()));
    }
}

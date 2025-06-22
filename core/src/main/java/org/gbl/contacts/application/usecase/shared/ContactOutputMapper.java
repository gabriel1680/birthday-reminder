package org.gbl.contacts.application.usecase.shared;

import org.gbl.contacts.domain.Contact;

public class ContactOutputMapper {

    public static ContactOutput toOutput(Contact contact) {
        return new ContactOutput(contact.id(), contact.name(), contact.birthdate());
    }
}

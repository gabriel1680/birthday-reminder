package org.gbl.contacts.application.usecase.get;

import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class GetContact {
    private final ContactRepository repository;

    public GetContact(ContactRepository repository) {
        this.repository = repository;
    }

    public ContactOutput execute(GetContactInput input) {
        final var contact = repository.getById(input.id())
                .orElseThrow(() -> new ContactNotFoundException(input.id()));
        return toOutput(contact);
    }

    private ContactOutput toOutput(Contact contact) {
        return new ContactOutput(contact.id(), contact.name(), contact.birthdate());
    }
}

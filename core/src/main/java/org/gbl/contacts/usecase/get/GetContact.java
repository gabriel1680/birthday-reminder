package org.gbl.contacts.usecase.get;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;

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

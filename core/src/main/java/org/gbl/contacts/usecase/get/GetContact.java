package org.gbl.contacts.usecase.get;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;

public class GetContact {
    private final ContactRepository repository;

    public GetContact(ContactRepository repository) {
        this.repository = repository;
    }

    public ContactOutput execute(GetContactRequest request) {
        if (!repository.has(request.number()))
            throw new ContactNotFoundException(request.number());
        return toOutput(repository.get(request.number()));
    }

    private ContactOutput toOutput(Contact contact) {
        return new ContactOutput(contact.name(), contact.number(), contact.birthdate());
    }
}

package org.gbl.contacts.usecase.add;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class AddContact {

    private final ContactRepository repository;

    public AddContact(ContactRepository repository) {
        this.repository = repository;
    }

    public void execute(AddContactRequest request) {
        if (repository.get(request.number()).isPresent())
            throw new ContactNumberAlreadyExistsException(request.number());
        final var contact = new Contact(request.name(), request.number(), request.birthdate());
        repository.add(contact);
    }
}

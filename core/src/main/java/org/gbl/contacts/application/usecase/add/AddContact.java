package org.gbl.contacts.application.usecase.add;

import org.gbl.contacts.application.service.IdProvider;
import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class AddContact {

    private final ContactRepository repository;
    private final IdProvider provider;

    public AddContact(ContactRepository repository, IdProvider provider) {
        this.repository = repository;
        this.provider = provider;
    }

    public AddContactOutput execute(AddContactInput input) {
        if (repository.existsByName(input.name()))
            throw new ContactAlreadyExistsException(input.name());
        final var contact = new Contact(provider.provideId(), input.name(), input.birthdate());
        repository.add(contact);
        return toOutput(contact);
    }

    private static AddContactOutput toOutput(Contact contact) {
        return new AddContactOutput(contact.id(), contact.name(), contact.birthdate());
    }
}

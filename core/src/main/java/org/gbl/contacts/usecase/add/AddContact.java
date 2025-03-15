package org.gbl.contacts.usecase.add;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;
import org.gbl.shared.IdProvider;

public class AddContact {

    private final ContactRepository repository;
    private final IdProvider provider;

    public AddContact(ContactRepository repository, IdProvider provider) {
        this.repository = repository;
        this.provider = provider;
    }

    public void execute(AddContactInput input) {
        if (repository.existsByName(input.name()))
            throw new ContactAlreadyExistsException(input.name());
        final var contact = new Contact(provider.provideId(), input.name(), input.birthdate());
        repository.add(contact);
    }
}

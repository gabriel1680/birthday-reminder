package org.gbl.contacts.application.usecase.add;

import org.gbl.contacts.application.service.IdProvider;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.usecase.shared.ContactOutputMapper;
import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class AddContact {

    private final ContactRepository repository;
    private final IdProvider provider;

    public AddContact(ContactRepository repository, IdProvider provider) {
        this.repository = repository;
        this.provider = provider;
    }

    public ContactOutput execute(AddContactInput input) {
        validateIfNameAlreadyTaken(input);
        final var contact = createContactFrom(input);
        repository.add(contact);
        return ContactOutputMapper.toOutput(contact);
    }

    private void validateIfNameAlreadyTaken(AddContactInput input) {
        if (repository.existsByName(input.name()))
            throw new ContactAlreadyExistsException(input.name());
    }

    private Contact createContactFrom(AddContactInput input) {
        return new Contact(provider.provideId(), input.name(), input.birthdate());
    }
}

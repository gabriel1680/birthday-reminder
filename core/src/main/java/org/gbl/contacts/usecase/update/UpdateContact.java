package org.gbl.contacts.usecase.update;

import org.gbl.contacts.usecase.InMemoryContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;

public class UpdateContact {
    private final InMemoryContactRepository repository;

    public UpdateContact(InMemoryContactRepository repository) {
        this.repository = repository;
    }

    public void execute(UpdateContactInput input) {
        final var contact = repository.getById(input.id())
                .orElseThrow(() -> new ContactNotFoundException(input.id()));
        if (input.name() != null && !input.name().isEmpty())
            contact.setName(input.name());
        if (input.birthdate() != null)
            contact.setBirthdate(input.birthdate());
        repository.update(contact);
    }
}

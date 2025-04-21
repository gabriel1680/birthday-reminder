package org.gbl.contacts.application.usecase.update;

import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.domain.ContactRepository;

public class UpdateContact {
    private final ContactRepository repository;

    public UpdateContact(ContactRepository repository) {
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

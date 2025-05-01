package org.gbl.contacts.application.usecase.remove;

import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.domain.ContactRepository;

public class RemoveContact {

    private final ContactRepository repository;

    public RemoveContact(ContactRepository repository) {
        this.repository = repository;
    }

    public void execute(RemoveContactInput input) {
        final var contact = repository.getById(input.id())
                .orElseThrow(() -> new ContactNotFoundException(input.id()));
        repository.remove(contact);
    }
}

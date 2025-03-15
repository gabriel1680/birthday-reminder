package org.gbl.contacts.usecase.remove;

import org.gbl.contacts.domain.ContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;

public class RemoveContact {

    private final ContactRepository repository;

    public RemoveContact(ContactRepository repository) {
        this.repository = repository;
    }

    public void execute(RemoveContactInput input) {
        final var contact = repository.getById(input.id())
                .orElseThrow(() -> new ContactNotFoundException(input.id()));
        repository.remove(contact.id());
    }
}

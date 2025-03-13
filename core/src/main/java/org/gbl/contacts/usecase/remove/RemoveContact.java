package org.gbl.contacts.usecase.remove;

import org.gbl.contacts.domain.ContactRepository;

public class RemoveContact {

    private final ContactRepository repository;

    public RemoveContact(ContactRepository repository) {
        this.repository = repository;
    }

    public void execute(RemoveContactRequest request) {
        if (!repository.has(request.number()))
            throw new ContactNotFoundException(request.number());
        repository.remove(request.number());
    }
}

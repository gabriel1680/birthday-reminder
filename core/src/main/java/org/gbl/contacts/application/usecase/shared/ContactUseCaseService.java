package org.gbl.contacts.application.usecase.shared;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class ContactUseCaseService {
    protected final ContactRepository repository;

    public ContactUseCaseService(ContactRepository repository) {
        this.repository = repository;
    }

    public Contact getOf(String id) {
        return repository.getById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }
}

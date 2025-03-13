package org.gbl;

public class AddContact {

    private final ContactRepository repository;

    public AddContact(ContactRepository repository) {
        this.repository = repository;
    }

    public void addContact(AddContactRequest request) {
        if (repository.has(request.number())) {
            throw new ContactNumberAlreadyExistsException(request.number());
        }
        final var contact = new Contact(request.name(), request.number(), request.birthdate());
        repository.add(contact);
    }
}

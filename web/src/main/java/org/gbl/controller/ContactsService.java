package org.gbl.controller;

import io.vavr.control.Either;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;

public class ContactsService {

    private final ContactsGateway contactsGateway;
    private final CreateContactValidator validator;

    public ContactsService(ContactsGateway contactsGateway) {
        this.contactsGateway = contactsGateway;
        this.validator = new CreateContactValidator();
    }

    public Either<InvalidContactFormException, ContactResponse> createContact(CreateContactForm form) {
        final var validation = validator.validate(form);
        if (validation.hasErrors()) {
            return Either.left(new InvalidContactFormException(validation));
        }
        final var request = new CreateContactRequest(validation.name(), validation.birthdate());
        return Either.right(contactsGateway.create(request));
    }

    public void delete(String contactId) {
        contactsGateway.delete(contactId);
    }

    public ContactResponse getOf(String contactId) {
        return contactsGateway.get(contactId);
    }
}

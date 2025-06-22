package org.gbl.contacts.application.usecase.update;

import org.gbl.contacts.application.usecase.shared.ContactUseCaseService;
import org.gbl.contacts.domain.ContactRepository;

public class UpdateContact extends ContactUseCaseService {

    public UpdateContact(ContactRepository repository) {
        super(repository);
    }

    public void execute(UpdateContactInput input) {
        final var contact = getOf(input.id());
        if (input.name() != null && !input.name().isEmpty())
            contact.setName(input.name());
        if (input.birthdate() != null)
            contact.setBirthdate(input.birthdate());
        repository.update(contact);
    }
}

package org.gbl.contacts.application.usecase.remove;

import org.gbl.contacts.application.usecase.shared.ContactUseCaseService;
import org.gbl.contacts.domain.ContactRepository;

public class RemoveContact extends ContactUseCaseService {

    public RemoveContact(ContactRepository repository) {
        super(repository);
    }

    public void execute(RemoveContactInput input) {
        repository.remove(getOf(input.id()));
    }
}

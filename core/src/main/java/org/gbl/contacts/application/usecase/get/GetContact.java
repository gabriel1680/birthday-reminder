package org.gbl.contacts.application.usecase.get;

import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.usecase.shared.ContactOutputMapper;
import org.gbl.contacts.application.usecase.shared.ContactUseCaseService;
import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

public class GetContact extends ContactUseCaseService {

    public GetContact(ContactRepository repository) {
        super(repository);
    }

    public ContactOutput execute(GetContactInput input) {
        return ContactOutputMapper.toOutput(getOf(input.id()));
    }
}

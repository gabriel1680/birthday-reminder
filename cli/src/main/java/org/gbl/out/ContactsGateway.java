package org.gbl.out;

import org.gbl.CreateContact.CreateContactRequest;

public interface ContactsGateway {
    CreateContactResponse create(CreateContactRequest request);
}

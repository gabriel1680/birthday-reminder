package org.gbl.out;

import org.gbl.in.CreateContact.CreateContactRequest;

public interface ContactsGateway {
    CreateContactResponse create(CreateContactRequest request);
}

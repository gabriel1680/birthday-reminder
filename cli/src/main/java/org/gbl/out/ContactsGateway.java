package org.gbl.out;

import org.gbl.in.CreateContact.CreateContactRequest;

public interface ContactsGateway {
    ContactResponse create(CreateContactRequest request);
    ContactResponse get(String contactId);
}

package org.gbl.out;

import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;

public interface ContactsGateway {
    ContactResponse create(CreateContactRequest request);

    ContactResponse get(String contactId);

    ContactResponse update(UpdateContactRequest request);
}

package org.gbl.out.http;

import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;

public class ContactsGatewayMock implements ContactsGateway {
    @Override
    public ContactResponse create(CreateContactRequest request) {
        return new ContactResponse("1", request.name, request.birthdate);
    }

    @Override
    public ContactResponse get(String contactId) {
        return new ContactResponse("1", "Bella", "13/09/1987");
    }

    @Override
    public ContactResponse update(UpdateContactRequest request) {
        return new ContactResponse("1", "John", "27/02/1998");
    }
}

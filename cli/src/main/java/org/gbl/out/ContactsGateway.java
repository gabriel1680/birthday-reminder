package org.gbl.out;

import io.vavr.control.Try;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;

public interface ContactsGateway {
    Try<ContactResponse> create(CreateContactRequest request);

    Try<ContactResponse> get(String contactId);

    Try<Void> update(UpdateContactRequest request);

    Try<Void> delete(String contactId);

    Try<Pagination<ContactResponse>> search(SearchRequest request);
}

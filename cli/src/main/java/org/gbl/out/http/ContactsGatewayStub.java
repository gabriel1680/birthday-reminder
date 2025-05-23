package org.gbl.out.http;

import io.vavr.control.Try;
import org.gbl.common.search.ContactFilter;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

import java.util.List;

public class ContactsGatewayStub implements ContactsGateway {

    private static final ContactResponse BELLA = new ContactResponse("1", "Bella", "13/09/1987");

    @Override
    public Try<ContactResponse> create(CreateContactRequest request) {
        return Try.success(new ContactResponse("1", request.name, request.birthdate));
    }

    @Override
    public Try<ContactResponse> get(String contactId) {
        return Try.success(BELLA);
    }

    @Override
    public Try<Void> update(UpdateContactRequest request) {
        return Try.success(null);
    }

    @Override
    public Try<Void> delete(String contactId) {
        return Try.success(null);
    }

    @Override
    public Try<Pagination<ContactResponse>> search(SearchRequest<ContactFilter> request) {
        return Try.success(new Pagination<>(request.page(), request.size(), 1, 1, List.of(BELLA)));
    }
}

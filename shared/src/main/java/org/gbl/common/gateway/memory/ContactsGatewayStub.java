package org.gbl.common.gateway.memory;

import io.vavr.control.Try;
import org.gbl.common.gateway.GetUpcomingBirthdaysRequest;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.common.gateway.UpdateContactRequest;

import java.time.LocalDate;
import java.util.List;

public class ContactsGatewayStub implements ContactsGateway {

    private static final ContactResponse BELLA = new ContactResponse("1", "Bella", LocalDate.of(1987, 9, 13));

    @Override
    public Try<ContactResponse> create(CreateContactRequest request) {
        return Try.success(new ContactResponse("1", request.name(), request.birthdate()));
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

    @Override
    public Try<List<ContactResponse>> getUpcomingBirthdays(GetUpcomingBirthdaysRequest request) {
        return Try.success(List.of(BELLA));
    }
}

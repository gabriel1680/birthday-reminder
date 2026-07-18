package org.gbl.common.gateway.memory;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.common.gateway.GetUpcomingBirthdaysRequest;
import org.gbl.common.gateway.UpdateContactRequest;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

import java.time.LocalDate;
import java.util.List;

public class ContactsGatewayStub implements ContactsGateway {

    private static final ContactResponse BELLA = new ContactResponse("1", "Bella", LocalDate.of(1987, 9, 13));

    @Override
    public ContactResponse create(CreateContactRequest request) {
        return new ContactResponse("1", request.name(), request.birthdate());
    }

    @Override
    public ContactResponse get(String contactId) {
        return BELLA;
    }

    @Override
    public void update(UpdateContactRequest request) {
    }

    @Override
    public void delete(String contactId) {
    }

    @Override
    public Pagination<ContactResponse> search(SearchRequest<ContactFilter> request) {
        return new Pagination<>(request.page(), request.size(), 1, 1, List.of(BELLA));
    }

    @Override
    public List<ContactResponse> getUpcomingBirthdays(GetUpcomingBirthdaysRequest request) {
        return List.of(BELLA);
    }
}

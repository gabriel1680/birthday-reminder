package org.gbl.common.gateway;

import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

import java.util.List;

public interface ContactsGateway {
    ContactResponse create(CreateContactRequest request);

    ContactResponse get(String contactId);

    void update(UpdateContactRequest request);

    void delete(String contactId);

    Pagination<ContactResponse> search(SearchRequest<ContactFilter> request);

    List<ContactResponse> getUpcomingBirthdays(GetUpcomingBirthdaysRequest request);
}

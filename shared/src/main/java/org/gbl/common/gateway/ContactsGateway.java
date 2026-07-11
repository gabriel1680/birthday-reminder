package org.gbl.common.gateway;

import io.vavr.control.Try;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

import java.util.List;

public interface ContactsGateway {
    Try<ContactResponse> create(CreateContactRequest request);

    Try<ContactResponse> get(String contactId);

    Try<ContactResponse> update(UpdateContactRequest request);

    Try<ContactResponse> delete(String contactId);

    Try<Pagination<ContactResponse>> search(SearchRequest<ContactFilter> request);

    Try<List<ContactResponse>> getUpcomingBirthdays(GetUpcomingBirthdaysRequest request);
}

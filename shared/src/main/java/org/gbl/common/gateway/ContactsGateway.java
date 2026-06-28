package org.gbl.common.gateway;

import io.vavr.control.Try;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

public interface ContactsGateway {
    Try<ContactResponse> create(CreateContactRequest request);

    Try<ContactResponse> get(String contactId);

    Try<Void> update(UpdateContactRequest request);

    Try<Void> delete(String contactId);

    Try<Pagination<ContactResponse>> search(SearchRequest<ContactFilter> request);
}

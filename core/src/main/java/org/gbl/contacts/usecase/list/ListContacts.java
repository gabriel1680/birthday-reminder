package org.gbl.contacts.usecase.list;

import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.shared.PaginationOutput;
import org.gbl.shared.SearchRequest;

public class ListContacts {

    private final ContactQueryRepository repository;

    public ListContacts(ContactQueryRepository repository) {
        this.repository = repository;
    }

    public PaginationOutput<ContactOutput> execute(SearchRequest<ContactFilter> request) {
        return repository.search(request);
    }
}

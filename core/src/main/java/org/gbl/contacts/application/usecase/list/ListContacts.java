package org.gbl.contacts.application.usecase.list;

import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;

public class ListContacts {

    private final ContactQueryRepository repository;

    public ListContacts(ContactQueryRepository repository) {
        this.repository = repository;
    }

    public PaginationOutput<ContactOutput> execute(SearchInput<ContactFilter> request) {
        return repository.search(request);
    }
}

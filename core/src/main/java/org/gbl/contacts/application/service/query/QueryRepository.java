package org.gbl.contacts.application.service.query;

public interface QueryRepository<O, F> {
    PaginationOutput<O> search(SearchInput<F> aRequest);
}

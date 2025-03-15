package org.gbl.shared;

public interface QueryRepository<O, F> {
    PaginationOutput<O> search(SearchInput<F> aRequest);
}

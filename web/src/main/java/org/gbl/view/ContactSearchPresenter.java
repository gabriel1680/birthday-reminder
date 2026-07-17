package org.gbl.view;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;

public class ContactSearchPresenter {

    public SearchViewModel toView(
            Pagination<ContactResponse> pagination,
            ContactFilter filter
    ) {
        final var window = PaginationWindowBuilder.from(pagination);
        final var contactViews = pagination.values().stream()
                .map(ContactViewPresenter::toView)
                .toList();
        final var paginationView = new PaginationView<>(
                window,
                pagination.total(),
                contactViews);
        return new SearchViewModel(
                paginationView,
                filter,
                new ContactUrlBuilder("/contacts")
        );
    }
}

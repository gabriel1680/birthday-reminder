package org.gbl.presenter;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.view.common.PaginationView;
import org.gbl.view.common.PaginationWindowBuilder;
import org.gbl.view.contacts.ContactUrlBuilder;
import org.gbl.view.contacts.SearchViewModel;

public class ContactSearchPresenter {

    private final ContactsPresenter contactsPresenter;

    public ContactSearchPresenter() {
        contactsPresenter = new ContactsPresenter();
    }

    public SearchViewModel toView(
            Pagination<ContactResponse> pagination,
            ContactFilter filter
    ) {
        final var window = PaginationWindowBuilder.from(pagination);
        final var contactViews = pagination.values().stream()
                .map(contactsPresenter::toView)
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

package org.gbl.view;

import org.gbl.SearchViewModel;
import org.gbl.UpcomingBirthday;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;

import java.util.List;

public class ContactSearchPresenter {

    public SearchViewModel toView(
            Pagination<ContactResponse> pagination,
            ContactFilter filter,
            List<UpcomingBirthday> upcoming
    ) {
        final var window = PaginationWindowBuilder.from(pagination);
        final var paginationView = new PaginationView<>(
                window,
                pagination.total(),
                pagination.values());
        return new SearchViewModel(
                paginationView,
                upcoming,
                filter,
                new ContactUrlBuilder("/")
        );
    }
}
package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;
import org.gbl.view.common.PaginationNavigation;
import org.gbl.view.common.PaginationView;

public record SearchViewModel(
        PaginationView<ContactViewModel> pagination,
        ContactFilter filter,
        PaginationNavigation<ContactFilter> paginationNavigation
) {}

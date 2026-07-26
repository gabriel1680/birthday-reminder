package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;
import org.gbl.view.common.pagination.PaginationNavigation;
import org.gbl.view.common.pagination.PaginationView;
import org.gbl.view.common.table.Table;

public record SearchViewModel(
        PaginationView<ContactViewModel> pagination,
        ContactFilter filter,
        PaginationNavigation<ContactFilter> paginationNavigation,
        Table table
) {}

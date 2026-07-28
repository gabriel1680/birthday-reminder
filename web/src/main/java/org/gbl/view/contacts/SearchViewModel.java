package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;
import org.gbl.view.components.pagination.PaginationNavigation;
import org.gbl.view.components.pagination.PaginationView;
import org.gbl.view.components.table.Table;

public record SearchViewModel(
        PaginationView<ContactViewModel> pagination,
        ContactFilter filter,
        PaginationNavigation<ContactFilter> paginationNavigation,
        Table table
) {}

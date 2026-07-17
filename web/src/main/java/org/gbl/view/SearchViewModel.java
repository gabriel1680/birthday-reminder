package org.gbl.view;

import org.gbl.common.search.ContactFilter;

public record SearchViewModel(
        PaginationView<ContactView> pagination,
        ContactFilter filter,
        ContactUrlBuilder urlBuilder
) {}

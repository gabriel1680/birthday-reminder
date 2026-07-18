package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;
import org.gbl.view.common.PaginationView;

public record SearchViewModel(
        PaginationView<ContactView> pagination,
        ContactFilter filter,
        ContactUrlBuilder urlBuilder
) {}

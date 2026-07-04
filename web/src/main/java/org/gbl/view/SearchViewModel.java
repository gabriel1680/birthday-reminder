package org.gbl.view;

import org.gbl.common.search.ContactFilter;

import java.util.Collection;

public record SearchViewModel(
        PaginationView<ContactView> pagination,
        Collection<UpcomingBirthday> upcomingBirthdays,
        ContactFilter filter,
        ContactUrlBuilder urlBuilder
) {}

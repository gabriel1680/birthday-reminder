package org.gbl.view;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;

import java.util.Collection;

public record SearchViewModel(
        PaginationView<ContactResponse> pagination,
        Collection<UpcomingBirthday> upcomingBirthdays,
        ContactFilter filter,
        ContactUrlBuilder urlBuilder
) {}

package org.gbl;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.view.ContactUrlBuilder;
import org.gbl.view.PaginationView;

import java.util.Collection;

public record SearchViewModel(
        PaginationView<ContactResponse> pagination,
        Collection<UpcomingBirthday> upcomingBirthdays,
        ContactFilter filter,
        ContactUrlBuilder urlBuilder
) {}

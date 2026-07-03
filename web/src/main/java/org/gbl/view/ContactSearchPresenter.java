package org.gbl.view;

import org.gbl.SearchViewModel;
import org.gbl.UpcomingBirthday;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class ContactSearchPresenter {

    private final Clock clock;

    public ContactSearchPresenter(Clock clock) {
        this.clock = clock;
    }

    public SearchViewModel toView(
            Pagination<ContactResponse> pagination,
            ContactFilter filter,
            List<ContactResponse> upcoming
    ) {
        final var window = PaginationWindowBuilder.from(pagination);
        final var paginationView = new PaginationView<>(
                window,
                pagination.total(),
                pagination.values());
        return new SearchViewModel(
                paginationView,
                upcoming.stream().map(this::toUpcomingBirthday)
                        .sorted(Comparator.comparing(UpcomingBirthday::birthdate))
                        .toList(),
                filter,
                new ContactUrlBuilder("/")
        );
    }

    private UpcomingBirthday toUpcomingBirthday(ContactResponse contactResponse) {
        final var today = LocalDate.now(clock);
        final var birthdate = LocalDate.parse(contactResponse.birthdate());
        final var nextBirthday = birthdate.withYear(today.getYear());
        final var daysUntil = ChronoUnit.DAYS.between(today, nextBirthday);
        return new UpcomingBirthday(
                contactResponse.id(),
                contactResponse.name(),
                contactResponse.birthdate(),
                (int) daysUntil
        );
    }
}
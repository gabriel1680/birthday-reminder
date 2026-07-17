package org.gbl.view;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        final var contactViews = pagination.values().stream()
                .map(ContactSearchPresenter::toContactView)
                .toList();
        final var paginationView = new PaginationView<>(
                window,
                pagination.total(),
                contactViews);
        return new SearchViewModel(
                paginationView,
                upcoming.stream().map(this::toUpcomingBirthday)
                        .sorted(Comparator.comparing(it -> it.contact().birthdate()))
                        .toList(),
                filter,
                new ContactUrlBuilder("/contacts")
        );
    }

    public static ContactView toContactView(ContactResponse c) {
        final var formatted = formatBirthday(c.birthdate());
        return new ContactView(c.id(), c.name(), formatted);
    }

    private static String formatBirthday(LocalDate birthday) {
        final var pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return birthday.format(pattern);
    }

    private UpcomingBirthday toUpcomingBirthday(ContactResponse contactResponse) {
        final var today = LocalDate.now(clock);
        final var nextBirthday = contactResponse.birthdate().withYear(today.getYear());
        final var daysUntil = (int) ChronoUnit.DAYS.between(today, nextBirthday);
        return new UpcomingBirthday(toContactView(contactResponse), daysUntil);
    }
}

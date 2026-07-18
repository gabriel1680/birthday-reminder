package org.gbl.view.contacts;

import org.gbl.common.gateway.ContactResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class UpcomingBirthdaysPresenter {

    private final Clock clock;

    public UpcomingBirthdaysPresenter(Clock clock) {
        this.clock = clock;
    }

    public List<UpcomingBirthday> toView(List<ContactResponse> contacts) {
        return contacts.stream()
                .map(this::toUpcomingBirthday)
                .sorted(Comparator.comparing(it -> it.contact().birthdate()))
                .toList();
    }

    private UpcomingBirthday toUpcomingBirthday(ContactResponse contact) {
        final var today = LocalDate.now(clock);
        final var nextBirthday = contact.birthdate().withYear(today.getYear());
        final var daysUntil = (int) ChronoUnit.DAYS.between(today, nextBirthday);
        return new UpcomingBirthday(ContactViewPresenter.toView(contact), daysUntil);
    }
}

package org.gbl.presenter;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.view.HomeViewModel;
import org.gbl.view.contacts.UpcomingBirthday;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class UpcomingBirthdaysPresenter {

    private final Clock clock;
    private final ContactsPresenter contactsPresenter;

    public UpcomingBirthdaysPresenter(Clock clock) {
        this.clock = clock;
        contactsPresenter = new ContactsPresenter();
    }

    public HomeViewModel toView(List<ContactResponse> contacts) {
        final var contactsViewModel = contacts.stream()
                .map(this::toUpcomingBirthday)
                .sorted(Comparator.comparing(it -> it.contact().birthdate()))
                .toList();
        return new HomeViewModel(contactsViewModel);
    }

    private UpcomingBirthday toUpcomingBirthday(ContactResponse contact) {
        final var today = LocalDate.now(clock);
        final var nextBirthday = contact.birthdate().withYear(today.getYear());
        final var daysUntil = (int) ChronoUnit.DAYS.between(today, nextBirthday);
        return new UpcomingBirthday(contactsPresenter.toView(contact), daysUntil);
    }
}

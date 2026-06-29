package org.gbl.contacts.application.usecase.upcoming_birthdays;

import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.usecase.shared.ContactOutput;

import java.time.Clock;
import java.util.Collection;

public class GetUpcomingBirthdays {

    private final Clock clock;
    private final ContactQueryRepository contactQueryRepository;

    public GetUpcomingBirthdays(Clock clock, ContactQueryRepository contactQueryRepository) {
        this.clock = clock;
        this.contactQueryRepository = contactQueryRepository;
    }

    public Collection<ContactOutput> execute(GetUpcomingBirthdaysInput input) {
        final var zonedToday = clock.instant().atZone(input.zoneId()).toLocalDate();
        return contactQueryRepository.upcomingBirthdaysFor(zonedToday, input.size());
    }
}

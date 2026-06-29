package org.gbl.contacts.application.service.query;

import org.gbl.contacts.application.usecase.shared.ContactOutput;

import java.time.LocalDate;
import java.util.Collection;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
    Collection<ContactOutput> upcomingBirthdaysFor(LocalDate instant, int size);
}

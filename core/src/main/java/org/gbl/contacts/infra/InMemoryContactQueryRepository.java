package org.gbl.contacts.infra;

import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.contacts.domain.Contact;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

import static java.util.Collections.*;

public class InMemoryContactQueryRepository implements ContactQueryRepository {

    private final Collection<Contact> contacts;

    public InMemoryContactQueryRepository(Collection<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public PaginationOutput<ContactOutput> search(SearchInput<ContactFilter> input) {
        if (contacts.isEmpty()) {
            return PaginationOutput.emptyOf(input.page(), input.size());
        }
        final var offset = input.offset();
        final var filter = input.filter();
        final var values = contacts
                .stream()
                .filter(c -> contactsWithFields(c, filter))
                .sorted(getComparator(input.order()))
                .skip(offset.start())
                .limit(offset.end())
                .map(InMemoryContactQueryRepository::toOutput)
                .toList();
        return new PaginationOutput<>(input.page(), input.size(), contacts.size(), values);
    }

    private static boolean contactsWithFields(Contact c, ContactFilter filter) {
        if (filter == null) {
            return true;
        }
        return (filter.name() == null || c.name().equals(filter.name()))
                && (filter.birthdateFilter() == null ||
                filter.birthdateFilter().isInValid() ||
                filter.birthdateFilter().contains(c.birthdate()));
    }

    private static Comparator<Contact> getComparator(SortingOrder order) {
        final var ascendingComparator = Comparator.comparing(Contact::name);
        return switch (order) {
            case ASC -> ascendingComparator;
            case DESC -> ascendingComparator.reversed();
        };
    }

    private static ContactOutput toOutput(Contact c) {
        return new ContactOutput(c.id(), c.name(), c.birthdate());
    }

    @Override
    public Collection<ContactOutput> upcomingBirthdaysFor(LocalDate date, int size) {
        if (contacts.isEmpty()) {
            return emptyList();
        }
        return contacts
                .stream()
                .filter(contact -> filterBirthdays(contact.birthdate(), date))
                .sorted(Comparator.comparing(c -> nextBirthday(c, date)))
                .limit(size)
                .map(InMemoryContactQueryRepository::toOutput)
                .toList();
    }

    private boolean filterBirthdays(LocalDate birthday, LocalDate date) {
        return !birthday.withYear(date.getYear()).isBefore(date);
    }

    private static LocalDate nextBirthday(Contact c, LocalDate date) {
        var next = c.birthdate().withYear(date.getYear());
        if (next.isBefore(date)) {
            next = next.plusYears(1);
        }
        return next;
    }
}

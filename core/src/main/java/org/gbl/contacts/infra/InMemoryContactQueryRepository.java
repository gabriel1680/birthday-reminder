package org.gbl.contacts.infra;

import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.domain.Contact;

import java.util.Comparator;
import java.util.List;

public class InMemoryContactQueryRepository
        extends InMemoryContactRepository
        implements ContactQueryRepository {

    public InMemoryContactQueryRepository(List<Contact> contacts) {
        super(contacts);
    }

    @Override
    public PaginationOutput<ContactOutput> search(SearchInput<ContactFilter> input) {
        final var contacts = contacts();
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
                && (filter.birthdate() == null || c.birthdate().equals(filter.birthdate()));
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
}

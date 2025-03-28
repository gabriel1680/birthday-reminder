package org.gbl.contacts.usecase.list;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.usecase.InMemoryContactRepository;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.shared.PaginationOutput;
import org.gbl.shared.SearchInput;

import java.time.LocalDate;
import java.util.List;

public class InMemoryContactQueryRepository
        extends InMemoryContactRepository
        implements ContactQueryRepository {

    public InMemoryContactQueryRepository(List<Contact> contacts) {
        super(contacts);
    }

    @Override
    public PaginationOutput<ContactOutput> search(SearchInput<ContactFilter> aRequest) {
        final var output = new ContactOutput("", "", LocalDate.now());
        final var list = List.of(output, output);
        return new PaginationOutput<>(aRequest.page(), aRequest.take(), 2, list);
    }
}

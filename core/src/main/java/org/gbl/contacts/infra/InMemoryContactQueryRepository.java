package org.gbl.contacts.infra;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;

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

package org.gbl.contacts.application.usecase.list;

import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.infra.InMemoryContactQueryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.JOHN_DOE;

class ListContactsTest {

    @Test
    void execute() {
        final var contacts = List.of(JOHN_DOE);
        final var queryContactRepository = new InMemoryContactQueryRepository(contacts);
        final var sut = new ListContacts(queryContactRepository);
        final var filter = ContactFilter.of();
        final var request = new SearchInput<>(1, 2, SortingOrder.ASC, filter);
        final var result = sut.execute(request);
        assertThat(result.page()).isEqualTo(request.page());
        assertThat(result.size()).isEqualTo(request.size());
        assertThat(result.total()).isEqualTo(1);
        assertThat(result.lastPage()).isEqualTo(1);
        assertThat(result.values()).hasSize(1);
    }
}
package org.gbl.contacts.usecase.list;

import org.gbl.contacts.infra.InMemoryContactQueryRepository;
import org.gbl.shared.SearchInput;
import org.gbl.shared.SortingOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.contacts.usecase.fixture.ContactFixture.JOHN_DOE;

class ListContactsTest {

    @Test
    void noPagination() {
        final var queryContactRepository = new InMemoryContactQueryRepository(List.of(JOHN_DOE));
        final var sut = new ListContacts(queryContactRepository);
        final var filter = new ContactFilter(null, null);
        final var request = new SearchInput<>(1, 2, 2, SortingOrder.ASC, filter);
        final var result = sut.execute(request);
        assertThat(result.page()).isEqualTo(request.page());
        assertThat(result.take()).isEqualTo(request.take());
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.values()).hasSize(2);
    }
}
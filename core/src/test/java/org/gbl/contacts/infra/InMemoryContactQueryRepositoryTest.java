package org.gbl.contacts.infra;

import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.domain.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryContactQueryRepositoryTest {

    private final Contact HARRY = new Contact("1", "Joan", LocalDate.of(2000, 12, 22));
    private final Contact TOM = new Contact("2", "Tom", LocalDate.of(2002, 12, 22));
    private final Collection<Contact> contacts = new ArrayList<>();

    private InMemoryContactQueryRepository sut;

    @BeforeEach
    void setUp() {
        sut = new InMemoryContactQueryRepository(contacts);
    }

    @Test
    void noContacts() {
        var input = new SearchInput<ContactFilter>(1, 1, SortingOrder.ASC, null);
        var output = sut.search(input);
        assertThat(output.values()).isEmpty();
    }

    @Test
    void oneContact() {
        contacts.add(HARRY);
        var input = new SearchInput<ContactFilter>(1, 1, SortingOrder.ASC, null);
        var output = sut.search(input);
        assertThat(output.values()).hasSize(1);
        assertThat(output.total()).isEqualTo(1);
    }

    @Nested
    class GivenTwoContacts {

        @BeforeEach
        void setUp() {
            contacts.addAll(List.of(HARRY, TOM));
        }

        @Test
        void paginated_first_page() {
            var input = new SearchInput<ContactFilter>(1, 1, SortingOrder.ASC, null);
            var output = sut.search(input);
            assertThat(output.values()).hasSize(1)
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(HARRY.id());
            assertThat(output.total()).isEqualTo(2);
        }

        @Test
        void paginated_second_page() {
            var input = new SearchInput<ContactFilter>(2, 1, SortingOrder.ASC, null);
            var output = sut.search(input);
            assertThat(output.values()).hasSize(1)
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(TOM.id());
            assertThat(output.total()).isEqualTo(2);
        }

        @Test
        void sorting_asc() {
            var input = new SearchInput<ContactFilter>(1, 2, SortingOrder.ASC, null);
            var output = sut.search(input);
            assertThat(output.values())
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(HARRY.id());
        }

        @Test
        void sorting_desc() {
            var input = new SearchInput<ContactFilter>(1, 2, SortingOrder.DESC, null);
            var output = sut.search(input);
            assertThat(output.values())
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(TOM.id());
        }

        @Test
        void filtering_by_name() {
            var input = new SearchInput<>(1, 2, SortingOrder.ASC, ContactFilter.of(HARRY.name()));
            var output = sut.search(input);
            assertThat(output.values())
                    .hasSize(1)
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(HARRY.id());
        }

        @Test
        void filtering_by_birthdate() {
            var input = new SearchInput<>(1, 2, SortingOrder.ASC, ContactFilter.of(TOM.birthdate()));
            var output = sut.search(input);
            assertThat(output.values())
                    .hasSize(1)
                    .first()
                    .extracting(ContactOutput::id)
                    .isEqualTo(TOM.id());
        }

        @Test
        void filtering_non_match() {
            var input = new SearchInput<>(1, 2, SortingOrder.ASC, ContactFilter.of(HARRY.name(), TOM.birthdate()));
            var output = sut.search(input);
            assertThat(output.values()).hasSize(0);
        }
    }
}
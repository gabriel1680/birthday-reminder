package org.gbl.contacts.application.usecase.add;

import org.gbl.contacts.infra.InMemoryContactRepository;
import org.gbl.contacts.application.service.SpyRandomIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.JOHN_DOE;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.toDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddContactTest {

    private InMemoryContactRepository contactRepository;
    private AddContact sut;
    private SpyRandomIdProvider idProvider;

    @BeforeEach
    void setUp() {
        final var contacts = List.of(JOHN_DOE);
        contactRepository = new InMemoryContactRepository(contacts);
        idProvider = new SpyRandomIdProvider();
        sut = new AddContact(contactRepository, idProvider);
    }

    @Test
    void shouldNotAddAContactWithSameNumber() {
        final var input = new AddContactInput(JOHN_DOE.name(), JOHN_DOE.birthdate());
        assertThatThrownBy(() -> sut.execute(input))
                .isInstanceOf(ContactAlreadyExistsException.class)
                .hasMessage("Contact already exists with name \"John Doe\"");
        assertThat(contactRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldAddAContact() {
        final var input = new AddContactInput("Mary Ann", toDate("19/09/1999"));
        final var output = sut.execute(input);
        assertThat(output)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting(AddContactOutput::name)
                .isEqualTo("Mary Ann");
        assertThat(contactRepository.size()).isEqualTo(2);
        assertTrue(contactRepository.getById(idProvider.lastIdProvided()).isPresent());
    }
}
package org.gbl.contacts.usecase.add;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.usecase.ContactFixture.JOHN_DOE;
import static org.gbl.contacts.usecase.ContactFixture.toDate;

public class AddContactTest {

    private InMemoryContactRepository contactRepository;
    private AddContact sut;

    @BeforeEach
    void setUp() {
        final var contacts = List.of(JOHN_DOE);
        contactRepository = new InMemoryContactRepository(contacts);
        sut = new AddContact(contactRepository);
    }

    @Test
    void shouldNotAddAContactWithSameNumber() {
        final var request = new AddContactRequest(JOHN_DOE.name(), JOHN_DOE.number(),
                                                  JOHN_DOE.birthdate());
        assertThatThrownBy(() -> sut.addContact(request))
                .isInstanceOf(ContactNumberAlreadyExistsException.class)
                .hasMessage("Contact already exists with number \"119991234\"");
        assertThat(contactRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldAddAContact() {
        final var request = new AddContactRequest("Mary Ann", "1198881234", toDate("19/09/1999"));
        sut.addContact(request);
        assertThat(contactRepository.size()).isEqualTo(2);
    }
}
package org.gbl.contacts.application.usecase.update;

import org.gbl.contacts.infra.InMemoryContactRepository;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.JOHN_DOE;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.toDate;

class UpdateContactTest {

    private InMemoryContactRepository contactRepository;
    private UpdateContact sut;

    @BeforeEach
    void setUp() {
        final var contacts = List.of(JOHN_DOE);
        contactRepository = new InMemoryContactRepository(contacts);
        sut = new UpdateContact(contactRepository);
    }

    @Test
    void contactNotFound() {
        final var input = new UpdateContactInput("99", null, null);
        assertThatThrownBy(() -> sut.execute(input))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact with id \"99\" not found");
    }

    @Test
    void updateNothing() {
        final var input = new UpdateContactInput(JOHN_DOE.id(), null, null);
        sut.execute(input);
        final var contact = contactRepository.getById(JOHN_DOE.id()).get();
        assertThat(contact.name()).isEqualTo(JOHN_DOE.name());
        assertThat(contact.birthdate()).isEqualTo(JOHN_DOE.birthdate());
    }

    @Test
    void updateOnlyName() {
        final var newName = "Johnny";
        final var input = new UpdateContactInput(JOHN_DOE.id(), newName, null);
        sut.execute(input);
        final var contact = contactRepository.getById(JOHN_DOE.id()).get();
        assertThat(contact.name()).isEqualTo(newName);
        assertThat(contact.birthdate()).isEqualTo(JOHN_DOE.birthdate());
    }

    @Test
    void updateOnlyBirthdate() {
        final var newBirthdate = toDate("27/02/1998");
        final var input = new UpdateContactInput(JOHN_DOE.id(), null, newBirthdate);
        sut.execute(input);
        final var contact = contactRepository.getById(JOHN_DOE.id()).get();
        assertThat(contact.name()).isEqualTo(JOHN_DOE.name());
        assertThat(contact.birthdate()).isEqualTo(newBirthdate);
    }
}
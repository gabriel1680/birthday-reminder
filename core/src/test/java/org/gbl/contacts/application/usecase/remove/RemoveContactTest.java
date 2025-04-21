package org.gbl.contacts.application.usecase.remove;

import org.gbl.contacts.infra.InMemoryContactRepository;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.JOHN_DOE;

class RemoveContactTest {

    private InMemoryContactRepository contactRepository;
    private RemoveContact sut;

    @BeforeEach
    void setUp() {
        contactRepository = new InMemoryContactRepository(List.of(JOHN_DOE));
        sut = new RemoveContact(contactRepository);
    }

    @Test
    void shouldNotRemoveAContactThatDoesNotExists() {
        assertThatThrownBy(() -> sut.execute(new RemoveContactInput("99")))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact with id \"99\" not found");
        assertThat(contactRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldRemoveAContact() {
        sut.execute(new RemoveContactInput(JOHN_DOE.id()));
        assertThat(contactRepository.size()).isEqualTo(0);
    }
}
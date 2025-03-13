package org.gbl.contacts.usecase.remove;

import org.gbl.contacts.usecase.InMemoryContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.usecase.ContactFixture.JOHN_DOE;

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
        assertThatThrownBy(() -> sut.execute(new RemoveContactRequest("119881234")))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact with number \"119881234\" not found");
        assertThat(contactRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldRemoveAContact() {
        sut.execute(new RemoveContactRequest(JOHN_DOE.number()));
        assertThat(contactRepository.size()).isEqualTo(0);
    }
}
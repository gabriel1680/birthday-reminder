package org.gbl.contacts.usecase.get;

import org.gbl.contacts.usecase.InMemoryContactRepository;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.usecase.fixture.ContactFixture.JOHN_DOE;

class GetContactTest {

    private InMemoryContactRepository contactRepository;
    private GetContact sut;

    @BeforeEach
    void setUp() {
        contactRepository = new InMemoryContactRepository(List.of(JOHN_DOE));
        sut = new GetContact(contactRepository);
    }

    @Test
    void shouldNotGetAContactThatDoesNotExists() {
        assertThatThrownBy(() -> sut.execute(new GetContactInput("99")))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact with id \"99\" not found");
    }

    @Test
    void shouldGetAContact() {
        final var request = new GetContactInput(JOHN_DOE.id());
        final var output = sut.execute(request);
        assertThat(output.name()).isEqualTo(JOHN_DOE.name());
        assertThat(output.name()).isEqualTo(JOHN_DOE.name());
        assertThat(output.birthdate()).isEqualTo(JOHN_DOE.birthdate());
    }
}
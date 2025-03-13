package org.gbl.contacts.usecase.get;

import org.gbl.contacts.usecase.InMemoryContactRepository;
import org.gbl.contacts.usecase.remove.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.usecase.ContactFixture.JOHN_DOE;

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
        assertThatThrownBy(() -> sut.execute(new GetContactRequest("1199888321")))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact with number \"1199888321\" not found");
    }

    @Test
    void shouldGetAContact() {
        final var request = new GetContactRequest(JOHN_DOE.number());
        final var output = sut.execute(request);
        assertThat(output.name()).isEqualTo(JOHN_DOE.name());
        assertThat(output.name()).isEqualTo(JOHN_DOE.name());
        assertThat(output.birthdate()).isEqualTo(JOHN_DOE.birthdate());
    }
}
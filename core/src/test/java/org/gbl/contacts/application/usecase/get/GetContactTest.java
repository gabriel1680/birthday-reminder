package org.gbl.contacts.application.usecase.get;

import org.gbl.contacts.application.usecase.fixture.ContactFixture;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.infra.InMemoryContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetContactTest {

    private GetContact sut;

    @BeforeEach
    void setUp() {
        var contacts = List.of(ContactFixture.JOHN_DOE);
        var contactRepository = new InMemoryContactRepository(contacts);
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
        var request = new GetContactInput(ContactFixture.JOHN_DOE.id());
        var output = sut.execute(request);
        assertThat(output.name()).isEqualTo(ContactFixture.JOHN_DOE.name());
        assertThat(output.name()).isEqualTo(ContactFixture.JOHN_DOE.name());
        assertThat(output.birthdate()).isEqualTo(ContactFixture.JOHN_DOE.birthdate());
    }
}
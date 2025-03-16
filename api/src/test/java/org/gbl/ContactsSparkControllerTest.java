package org.gbl;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.add.ContactAlreadyExistsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.gbl.SparkResponseAssertionBuilder.aAssertionFor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactsSparkControllerTest extends SparkControllerTest {

    @Mock
    ContactsModule contactsModule;
    @InjectMocks
    ContractsSparkController sut;

    @Nested
    class CreateContactShould {

        private static final String VALID_PAYLOAD = "{\"name\": \"Maria\", \"birthdate\": " +
                "\"2018-11-15T00:00:00\"}";

        @Test
        void invalidPayload() {
            when(request.body()).thenReturn("{}");
            final var output = sut.createContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid payload", null)
                    .withActual(output)
                    .build();
        }

        @Test
        void throwTheUnknownError() {
            when(request.body()).thenReturn(VALID_PAYLOAD);
            doThrow(new RuntimeException("Internal error")).when(contactsModule).addContact(any());
            assertThatThrownBy(() -> sut.createContact(request, response))
                    .hasMessage("Internal error")
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void parseAnErrorResponse_whenAApplicationExceptionIsThrown() {
            when(request.body()).thenReturn(VALID_PAYLOAD);
            final var exception = new ContactAlreadyExistsException("Contact 1");
            doThrow(exception).when(contactsModule).addContact(any());
            final var output = sut.createContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(UNPROCESSABLE_ENTITY)
                    .forExpected(ResponseStatus.ERROR, exception.getMessage(), null)
                    .withActual(output)
                    .build();
        }

        @Captor
        ArgumentCaptor<AddContactInput> captor;

        @Test
        void createAContact() {
            when(request.body()).thenReturn(VALID_PAYLOAD);
            final var output = sut.createContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(CREATED)
                    .forExpected(ResponseStatus.SUCCESS, "", null)
                    .withActual(output)
                    .build();
            verify(contactsModule).addContact(captor.capture());
            assertThat(captor.getValue().name()).isEqualTo("Maria");
            assertThat(captor.getValue().birthdate()).isEqualTo(LocalDate.of(2018, 11, 15));
        }
    }
}
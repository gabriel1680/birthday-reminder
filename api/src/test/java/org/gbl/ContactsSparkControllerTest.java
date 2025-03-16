package org.gbl;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.get.GetContactInput;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;
import org.json.JSONObject;
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
import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;
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
        void throwAParseError_whenReceiveAnInvalidPayload() {
            when(request.body()).thenReturn("{}");
            final var output = sut.createContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid payload", new JSONObject())
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
                    .forExpected(ResponseStatus.ERROR, exception.getMessage(), new JSONObject())
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
                    .forExpected(ResponseStatus.SUCCESS, "", new JSONObject())
                    .withActual(output)
                    .build();
            verify(contactsModule).addContact(captor.capture());
            assertThat(captor.getValue().name()).isEqualTo("Maria");
            assertThat(captor.getValue().birthdate()).isEqualTo(LocalDate.of(2018, 11, 15));
        }
    }

    @Nested
    class GetContractShould {

        @Test
        void throwAParseError_whenReceiveAnInvalidId() {
            when(request.params("id")).thenReturn("");
            final var output = sut.getContract(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid id", new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Test
        void throwTheUnknownError() {
            when(request.params("id")).thenReturn("123");
            doThrow(new RuntimeException("Internal error")).when(contactsModule).getContact(any());
            assertThatThrownBy(() -> sut.getContract(request, response))
                    .hasMessage("Internal error")
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void parseAnErrorResponse_whenAApplicationExceptionIsThrown() {
            when(request.params("id")).thenReturn("123");
            final var exception = new ContactNotFoundException("123");
            doThrow(exception).when(contactsModule).getContact(any());
            final var output = sut.getContract(request, response);
            aAssertionFor(response)
                    .withStatusCode(NOT_FOUND)
                    .forExpected(ResponseStatus.ERROR, exception.getMessage(), new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Captor
        ArgumentCaptor<GetContactInput> captor;

        @Test
        void getAContact() {
            when(request.params("id")).thenReturn("123");
            final var contact = new ContactOutput("123", "Mary", LocalDate.now());
            when(contactsModule.getContact(any())).thenReturn(contact);
            final var output = sut.getContract(request, response);
            final var data = new JSONObject()
                    .put("id", contact.id())
                    .put("name", contact.name())
                    .put("birthdate", contact.birthdate());
            aAssertionFor(response)
                    .withStatusCode(OK)
                    .forExpected(ResponseStatus.SUCCESS, "", data)
                    .withActual(output)
                    .build();
            verify(contactsModule).getContact(captor.capture());
            assertThat(captor.getValue().id()).isEqualTo("123");
        }
    }
}
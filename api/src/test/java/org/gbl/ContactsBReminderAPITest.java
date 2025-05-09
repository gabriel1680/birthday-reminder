package org.gbl;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.add.AddContactOutput;
import org.gbl.contacts.application.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.controller.contacts.ContactsAPI;
import org.gbl.controller.contacts.ContactsAPIProxy;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.gbl.SparkResponseAssertionBuilder.aAssertionFor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactsBReminderAPITest extends SparkControllerTest {

    @Mock
    ContactsModule contactsModule;

    @InjectMocks
    ContactsAPIProxy sut;

    @Nested
    class CreateContactShould {

        private static final String VALID_PAYLOAD = """
                {
                    "name": "Maria",
                    "birthdate": "2018-11-15T00:00:00"
                }
                """;

        private static final AddContactOutput maria = new AddContactOutput("1", "Maria", toDate("2018-11-15"));

        private static LocalDate toDate(String date) {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        }

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
            when(contactsModule.addContact(any())).thenReturn(maria);
            when(request.body()).thenReturn(VALID_PAYLOAD);
            final var output = sut.createContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(CREATED)
                    .forExpected(ResponseStatus.SUCCESS, "", toJson(maria))
                    .withActual(output)
                    .build();
            verify(contactsModule).addContact(captor.capture());
            assertThat(captor.getValue().name()).isEqualTo("Maria");
            assertThat(captor.getValue().birthdate()).isEqualTo(toDate("2018-11-15"));
        }

        private static JSONObject toJson(AddContactOutput maria) {
            return new JSONObject()
                    .put("id", maria.id())
                    .put("name", maria.name())
                    .put("birthdate", maria.birthdate());
        }
    }

    @Nested
    class GetContactShould {

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

    @Nested
    class DeleteContactShould {

        @Test
        void throwAParseError_whenReceiveAnInvalidId() {
            when(request.params("id")).thenReturn("");
            final var output = sut.deleteContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid id", new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Test
        void throwTheUnknownError() {
            when(request.params("id")).thenReturn("123");
            doThrow(new RuntimeException("Internal error")).when(contactsModule).removeContact(any());
            assertThatThrownBy(() -> sut.deleteContact(request, response))
                    .hasMessage("Internal error")
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void parseAnErrorResponse_whenAApplicationExceptionIsThrown() {
            when(request.params("id")).thenReturn("123");
            final var exception = new ContactNotFoundException("123");
            doThrow(exception).when(contactsModule).removeContact(any());
            final var output = sut.deleteContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(NOT_FOUND)
                    .forExpected(ResponseStatus.ERROR, exception.getMessage(), new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Captor
        ArgumentCaptor<RemoveContactInput> captor;

        @Test
        void deleteAContact() {
            when(request.params("id")).thenReturn("123");
            final var output = sut.deleteContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(NO_CONTENT)
                    .forExpected(ResponseStatus.SUCCESS, "", new JSONObject())
                    .withActual(output)
                    .build();
            verify(contactsModule).removeContact(captor.capture());
            assertThat(captor.getValue().id()).isEqualTo("123");
        }
    }

    @Nested
    class UpdateContactShould {

        private static final String VALID_PAYLOAD = "{\"name\": \"Maria\", \"birthdate\": " +
                "\"2018-11-15T00:00:00\"}";

        @Test
        void throwAParseError_whenReceiveAnInvalidId() {
            when(request.params("id")).thenReturn("");
            final var output = sut.updateContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid id", new JSONObject())
                    .withActual(output)
                    .build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"{}", "{\"name\": \"\"}", "{\"birthdate\":\"\"}"})
        void throwAParseError_whenReceiveAnInvalidPayload(String body) {
            when(request.params("id")).thenReturn("123");
            when(request.body()).thenReturn(body);
            final var output = sut.updateContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid payload", new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Test
        void throwTheUnknownError() {
            when(request.params("id")).thenReturn("123");
            when(request.body()).thenReturn(VALID_PAYLOAD);
            doThrow(new RuntimeException("Internal error")).when(contactsModule).updateContact(any());
            assertThatThrownBy(() -> sut.updateContact(request, response))
                    .hasMessage("Internal error")
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void parseAnErrorResponse_whenAApplicationExceptionIsThrown() {
            when(request.params("id")).thenReturn("123");
            when(request.body()).thenReturn(VALID_PAYLOAD);
            final var exception = new ContactNotFoundException("123");
            doThrow(exception).when(contactsModule).updateContact(any());
            final var output = sut.updateContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(NOT_FOUND)
                    .forExpected(ResponseStatus.ERROR, exception.getMessage(), new JSONObject())
                    .withActual(output)
                    .build();
        }

        @Captor
        ArgumentCaptor<UpdateContactInput> captor;

        @Test
        void updateContact() {
            when(request.params("id")).thenReturn("123");
            when(request.body()).thenReturn(VALID_PAYLOAD);
            final var output = sut.updateContact(request, response);
            aAssertionFor(response)
                    .withStatusCode(NO_CONTENT)
                    .forExpected(ResponseStatus.SUCCESS, "", new JSONObject())
                    .withActual(output)
                    .build();
            verify(contactsModule).updateContact(captor.capture());
            final var input = captor.getValue();
            assertThat(input.id()).isEqualTo("123");
            assertThat(input.name()).isEqualTo("Maria");
            assertThat(input.birthdate()).isEqualTo("2018-11-15");
        }
    }

    @Nested
    class GetAllContactsShould {

        @Test
        void listEmptyContacts() {
            doAnswer(invocationOnMock -> invocationOnMock.getArgument(1))
                    .when(request)
                    .queryParamOrDefault(anyString(), anyString());
            when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 15));
            var output = sut.searchContacts(request, response);
            var json = """
                    {"total":0,"size":15,"last_page":1,"values":[],"current_page":1}""";
            aAssertionFor(response)
                    .withStatusCode(OK)
                    .forExpected(ResponseStatus.SUCCESS, "", json)
                    .withActual(output)
                    .build();
        }

        @Test
        void notEmptyResponse() {
            doAnswer(invocationOnMock -> invocationOnMock.getArgument(1))
                    .when(request)
                    .queryParamOrDefault(anyString(), anyString());
            var results = List.of(new ContactOutput("1", "a", LocalDate.of(2018, 4, 9)));
            when(contactsModule.listContacts(any())).thenReturn(new PaginationOutput<>(1, 1, 1, results));
            var output = sut.searchContacts(request, response);
            var json = """
                    {"total":1,"size":1,"last_page":1,"values":[{"birthdate":"2018-04-09","name":"a","id":"1"}],"current_page":1}""";
            aAssertionFor(response)
                    .withStatusCode(OK)
                    .forExpected(ResponseStatus.SUCCESS, "", json)
                    .withActual(output)
                    .build();
        }

        @Captor
        private ArgumentCaptor<SearchInput<ContactFilter>> inputArgumentCaptor;

        @Test
        void requestPagination() {
            when(request.queryParamOrDefault("page", "1")).thenReturn("1");
            when(request.queryParamOrDefault("size", "5")).thenReturn("5");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("desc");
            when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 1));
            sut.searchContacts(request, response);
            verify(contactsModule).listContacts(inputArgumentCaptor.capture());
            var input = inputArgumentCaptor.getValue();
            assertThat(input)
                    .extracting(SearchInput::page, SearchInput::size, SearchInput::order)
                    .containsExactly(1, 5, SortingOrder.DESC);
        }

        @Test
        void requestNameFilter() {
            when(request.queryParams("filter_name")).thenReturn("a");
            when(request.queryParams("filter_birthdate")).thenReturn(null);
            when(request.queryParamOrDefault("page", "1")).thenReturn("1");
            when(request.queryParamOrDefault("size", "5")).thenReturn("5");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("desc");
            when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 1));
            sut.searchContacts(request, response);
            verify(contactsModule).listContacts(inputArgumentCaptor.capture());
            var input = inputArgumentCaptor.getValue();
            assertThat(input)
                    .extracting(SearchInput::filter)
                    .isInstanceOf(ContactFilter.class)
                    .isEqualTo(ContactFilter.of("a"));
        }

        @Test
        void requestBirthdateFilter() {
            when(request.queryParams("filter_name")).thenReturn("a");
            when(request.queryParams("filter_birthdate")).thenReturn("2018-11-15");
            when(request.queryParamOrDefault("page", "1")).thenReturn("1");
            when(request.queryParamOrDefault("size", "5")).thenReturn("5");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("desc");
            when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 1));
            sut.searchContacts(request, response);
            verify(contactsModule).listContacts(inputArgumentCaptor.capture());
            var input = inputArgumentCaptor.getValue();
            assertThat(input)
                    .extracting(SearchInput::filter)
                    .isInstanceOf(ContactFilter.class)
                    .isEqualTo(ContactFilter.of("a", LocalDate.of(2018, 11, 15)));
        }

        @Test
        void requestNoFilter() {
            when(request.queryParams("filter_name")).thenReturn(null);
            when(request.queryParams("filter_birthdate")).thenReturn(null);
            when(request.queryParamOrDefault("page", "1")).thenReturn("1");
            when(request.queryParamOrDefault("size", "5")).thenReturn("5");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("desc");
            when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 1));
            sut.searchContacts(request, response);
            verify(contactsModule).listContacts(inputArgumentCaptor.capture());
            var input = inputArgumentCaptor.getValue();
            assertThat(input)
                    .extracting(SearchInput::filter)
                    .isInstanceOf(ContactFilter.class)
                    .isEqualTo(ContactFilter.of());
        }

        @Test
        void invalidSearchPayload() {
            when(request.queryParamOrDefault("page", "1")).thenReturn("0");
            when(request.queryParamOrDefault("size", "5")).thenReturn("0");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("asc");
            final var output = sut.searchContacts(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "invalid page: value should be greater than 0", new JSONObject())
                    .withActual(output)
                    .build();
            verify(contactsModule, never()).listContacts(any());
        }

        @Test
        void invalidSearchOrder() {
            when(request.queryParamOrDefault("page", "1")).thenReturn("1");
            when(request.queryParamOrDefault("size", "5")).thenReturn("1");
            when(request.queryParamOrDefault("order", "asc")).thenReturn("asd");
            final var output = sut.searchContacts(request, response);
            aAssertionFor(response)
                    .withStatusCode(BAD_REQUEST)
                    .forExpected(ResponseStatus.ERROR, "Invalid SortingOrder string value", new JSONObject())
                    .withActual(output)
                    .build();
            verify(contactsModule, never()).listContacts(any());
        }
    }
}
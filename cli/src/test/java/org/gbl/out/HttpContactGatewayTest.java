package org.gbl.out;

import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;
import org.gbl.out.http.ApiResponse;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.common.service.json.GsonJsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpContactGatewayTest {

    private static final String BASE_URL = "https://localhost:8000";

    private final GsonJsonParser jsonParser = new GsonJsonParser();

    @Mock
    private HttpResponse<String> response;

    @Mock
    private HttpClient client;

    private HttpContactGateway sut;

    @BeforeEach
    void setUp() {
        sut = new HttpContactGateway(jsonParser, client, BASE_URL);
    }

    @Test
    void responseError() throws IOException, InterruptedException {
        when(response.statusCode()).thenReturn(400);
        when(response.body()).thenReturn(jsonParser.stringify(new ApiResponse<String>("error", "invalid" +
                " " +
                "name", null)));
        when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        var request = new CreateContactRequest("John", "1957-04-14");
        final var output = sut.create(request);
        assertThat(output.isFailure()).isTrue();
        assertThat(output.failed().get())
                .extracting(Throwable::getMessage)
                .isEqualTo("invalid name");
    }

    @Test
    void clientThrowsError() throws IOException, InterruptedException {
        final var error = new IOException("invalid error");
        when(client.send(any(), any())).thenThrow(error);
        var request = new CreateContactRequest("John", "1957-04-14");
        assertThat(sut.create(request).isFailure()).isTrue();
    }

    @Nested
    class WhenCreateAContact {

        private final CreateContactRequest request =
                new CreateContactRequest("Mary Ann", "1959-08-14");

        @Captor
        private ArgumentCaptor<HttpRequest> requestCaptor;

        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(201);
            final var body = """
                    {
                    "status": "success",
                    "message": "contact created",
                    "data": { "id": 1, "name":"Mary Ann","birthdate":"1959-08-14" }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.create(request).get())
                    .isNotNull()
                    .isInstanceOf(ContactResponse.class);
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.create(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest.method()).isEqualTo("POST");
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts");
            assertThat(httpRequest)
                    .extracting(HttpRequest::bodyPublisher)
                    .extracting(Optional::get)
                    .extracting(HttpRequest.BodyPublisher::contentLength)
                    .isEqualTo(44L);
        }
    }

    @Nested
    class WhenGetAContact {

        private final String request = "1";

        @Captor
        private ArgumentCaptor<HttpRequest> requestCaptor;

        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(200);
            final var body = """
                    {
                    "status": "success",
                    "message": "",
                    "data": { "name":"Mary Ann","birthdate":"1959-08-14" }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.get(request).get())
                    .isNotNull()
                    .isInstanceOf(ContactResponse.class);
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.get(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest.method()).isEqualTo("GET");
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts/1");
        }
    }

    @Nested
    class WhenUpdateAContact {

        private final UpdateContactRequest request =
                new UpdateContactRequest("2", "John Wick", "1964-09-02");

        @Captor
        private ArgumentCaptor<HttpRequest> requestCaptor;

        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(200);
            final var body = """
                    {
                    "status": "success",
                    "message": "Contact updated",
                    "data": { "id":"2","name":"John Wick","birthdate":"1964-09-02" }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.update(request).get()).isNull();
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.update(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest.method()).isEqualTo("PUT");
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts/2");
            assertThat(httpRequest)
                    .extracting(HttpRequest::bodyPublisher)
                    .extracting(Optional::get)
                    .extracting(HttpRequest.BodyPublisher::contentLength)
                    .isEqualTo(54L);
        }
    }

    @Nested
    class WhenDeleteAContact {

        private final String request = "1";

        @Captor
        private ArgumentCaptor<HttpRequest> requestCaptor;

        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(204);
            final var body = """
                    {
                    "status": "success",
                    "message": "contact deleted",
                    "data": {}
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void success() {
            assertThat(sut.delete(request).isSuccess()).isTrue();
        }

        @Test
        void invalidPayload() throws IOException, InterruptedException {
            sut.delete(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest.method()).isEqualTo("DELETE");
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts/1");
        }
    }

    @Nested
    class WhenSearchContacts {

        private final SearchRequest request = new SearchRequest(1, 1, SortingOrder.ASC, null);

        @Captor
        private ArgumentCaptor<HttpRequest> requestCaptor;

        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(200);
            final var body = """
                    {
                    "status": "success",
                    "message": "",
                    "data": {
                        "page": 1,
                        "size": 5,
                        "lastPage": 5,
                        "total": 20,
                        "values": [{"id":"1", "name":"Mary Ann","birthdate":"1959-08-14" }]
                    }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.search(request).get())
                    .isNotNull()
                    .isInstanceOf(Pagination.class);
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.search(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest.method()).isEqualTo("GET");
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts");
        }
    }
}
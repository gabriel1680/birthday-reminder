package org.gbl.out;

import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.out.http.ApiResponse;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.utils.JSON;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpContactGatewayTest {

    private static final String BASE_URL = "https://localhost:8000";

    @Mock
    private HttpResponse<String> response;

    @Mock
    private HttpClient client;

    private HttpContactGateway sut;

    @BeforeEach
    void setUp() {
        sut = new HttpContactGateway(client, BASE_URL);
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
                    "status": 201,
                    "message": "success",
                    "data": { "name":"Mary Ann","birthdate":"1959-08-14" }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.create(request))
                    .isNotNull()
                    .isInstanceOf(ContactResponse.class);
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.create(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
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

    @Test
    void responseError() throws IOException, InterruptedException {
        when(response.statusCode()).thenReturn(400);
        when(response.body()).thenReturn(JSON.stringify(new ApiResponse<String>(400, "invalid " +
                "name", null)));
        when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        var request = new CreateContactRequest("John", "1957-04-14");
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("invalid name");
    }

    @Test
    void clientThrowsError() throws IOException, InterruptedException {
        final var error = new IOException("invalid error");
        when(client.send(any(), any())).thenThrow(error);
        var request = new CreateContactRequest("John", "1957-04-14");
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error during request")
                .hasCause(error);
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
                    "status": 200,
                    "message": "success",
                    "data": { "name":"Mary Ann","birthdate":"1959-08-14" }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.get(request))
                    .isNotNull()
                    .isInstanceOf(ContactResponse.class);
        }

        @Test
        void shouldCallSendWithAValidRequest() throws IOException, InterruptedException {
            sut.get(request);
            verify(client).send(requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest)
                    .extracting(HttpRequest::uri)
                    .extracting(URI::getPath)
                    .isEqualTo("/contacts/1");
        }
    }
}
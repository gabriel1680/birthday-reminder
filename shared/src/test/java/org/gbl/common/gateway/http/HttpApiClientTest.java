package org.gbl.common.gateway.http;

import com.google.gson.reflect.TypeToken;
import io.vavr.control.Try;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.common.service.json.GsonJsonAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpApiClientTest {

    private static final String BASE_URL = "https://localhost:8000";
    private final GsonJsonAdapter jsonParser = new GsonJsonAdapter();

    @Captor
    private ArgumentCaptor<HttpRequest> requestCaptor;

    @Mock
    private HttpResponse<String> response;

    @Mock
    private HttpClient client;

    private HttpApiClient sut;


    @BeforeEach
    void setUp() {
        sut = new HttpApiClient(jsonParser, client, BASE_URL);
    }

    private void setupMockResponsesWithStatus(int statusCode) throws IOException, InterruptedException {
        setupMockResponseWith(statusCode, "a data");
    }

    private void setupMockResponseWith(int statusCode, Object data) throws IOException, InterruptedException {
        when(response.statusCode()).thenReturn(statusCode);
        final var apiResponse = new ApiResponse<>("a status", "a message", data);
        when(response.body()).thenReturn(jsonParser.stringify(apiResponse));
        when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
    }

    @Test
    void client_exception() throws IOException, InterruptedException {
        final var error = new IOException("invalid error");
        when(client.send(any(), any())).thenThrow(error);
        assertThat(sut.get("/", String.class).isFailure()).isTrue();
    }

    @Nested
    class RawApiResponseParsing {

        @Test
        void should_parse_empty_data() throws IOException, InterruptedException {
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
            final var response = sut.delete("/", ContactResponse.class);
            assertThat(response.isSuccess()).isTrue();
        }

        @Test
        void should_parse_real_data() throws IOException, InterruptedException {
            when(response.statusCode()).thenReturn(204);
            final var body = """
                    {
                    "status": "success",
                    "message": "contact created",
                    "data": { "values": [{ "id": 1, "name":"Mary Ann","birthdate":"1959-08-14" }] }
                    }
                    """;
            when(response.body()).thenReturn(body);
            when(client.send(any(), any(BodyHandler.class))).thenReturn(response);
            final var response = sut.post("/", new Object(), ContactResponse.class);
            assertThat(response.get()).isInstanceOf(ContactResponse.class);
        }
    }

    @Test
    void should_build_base_url_plus_path() throws IOException, InterruptedException {
        setupMockResponsesWithStatus(200);
        sut.get("/a-path", String.class);
        verify(client).send(requestCaptor.capture(), any());
        assertThat(requestCaptor.getValue()).extracting(HttpRequest::uri)
                .isEqualTo(URI.create(BASE_URL + "/a-path"));
    }
    @Nested
    class ParseResponse {

        @Test
        void should_parse_a_complex_object() throws IOException, InterruptedException {
            final var uuid = UUID.randomUUID();
            setupMockResponseWith(200, List.of(uuid));
            final var type = TypeToken.getParameterized(List.class, UUID.class).getType();
            final Try<List<UUID>> result = sut.post("/", new Object(), type);
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).first().extracting(UUID::toString).isEqualTo(uuid.toString());
        }

    }
    @Nested
    class ValidateStatusCode {

        @ParameterizedTest
        @ValueSource(ints = {400, 401, 500})
        void when_not_success_status_code_should_throw(int statusCode) throws IOException, InterruptedException {
            setupMockResponsesWithStatus(statusCode);
            final var output = sut.post("/", new Object(), String.class);
            assertThat(output.isFailure()).isTrue();
            assertThat(output.failed().get())
                    .extracting(Throwable::getMessage)
                    .isEqualTo("a message");
        }

        @ParameterizedTest
        @ValueSource(ints = {200, 201, 204})
        void when_success_status_code_should_succeed(int statusCode) throws IOException, InterruptedException {
            setupMockResponsesWithStatus(statusCode);
            final var output = sut.post("/", new Object(), String.class);
            assertThat(output.isSuccess()).isTrue();
            assertThat(output.get()).isInstanceOf(String.class);
        }

        @Test
        void when_404_should_fail_with_not_found_exception() throws IOException, InterruptedException {
            setupMockResponsesWithStatus(404);
            final var output = sut.post("/", new Object(), String.class);
            assertThat(output.isFailure()).isTrue();
            assertThat(output.failed().get()).isInstanceOf(ResourceNotFoundException.class);
        }

    }

    private void assertHttpPathAndVerb(String path, String method) throws IOException, InterruptedException {
        verify(client, times(1)).send(requestCaptor.capture(), any());
        final var value = requestCaptor.getValue();
        assertThat(value).extracting(HttpRequest::uri)
                .extracting(URI::getPath)
                .isEqualTo(path);
        assertThat(value).extracting(HttpRequest::method).isEqualTo(method);
    }

    @Test
    void should_get_without_headers() throws IOException, InterruptedException {
        setupMockResponseWith(200, "xx");
        final Try<String> result = sut.get("/a-path", String.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo("xx");
        assertHttpPathAndVerb("/a-path", "GET");
    }

    @Test
    void should_get_with_headers() throws IOException, InterruptedException {
        setupMockResponseWith(200, "xx");
        sut.get("/", String.class, Map.of("Content-Type", "text/html"));
        verify(client, times(1)).send(requestCaptor.capture(), any());
        assertThat(requestCaptor.getValue().headers().firstValue("Content-Type"))
                .get()
                .isEqualTo("text/html");
    }

    @Test
    void should_post_with_response() throws IOException, InterruptedException {
        setupMockResponseWith(200, "zz");
        final Try<String> result = sut.post("/to-create", new Object(), String.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo("zz");
        assertHttpPathAndVerb("/to-create", "POST");
    }

    @Test
    void should_post_with_body() throws IOException, InterruptedException {
        setupMockResponsesWithStatus(200);
        final var body = List.of(Map.of("id", "123"));
         sut.post("/to-create", body, String.class);
        verify(client, times(1)).send(requestCaptor.capture(), any());
        final var value = requestCaptor.getValue();
        assertThat(value.bodyPublisher())
                .get()
                .extracting(BodyPublisher::contentLength)
                .isEqualTo(14L);
    }

    @Test
    void should_put_with_response() throws IOException, InterruptedException {
        setupMockResponseWith(200, "yy");
        final Try<String> result = sut.put("/to-update", new Object(), String.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo("yy");
        assertHttpPathAndVerb("/to-update", "PUT");
    }

    @Test
    void should_put_with_body() throws IOException, InterruptedException {
        setupMockResponsesWithStatus(200);
        final var body = List.of(Map.of("id", "123"));
        sut.put("/to-update", body, String.class);
        verify(client, times(1)).send(requestCaptor.capture(), any());
        final var value = requestCaptor.getValue();
        assertThat(value.bodyPublisher())
                .get()
                .extracting(BodyPublisher::contentLength)
                .isEqualTo(14L);
    }

    @Test
    void should_delete_and_return_data() throws IOException, InterruptedException {
        setupMockResponseWith(200, "xx");
        final Try<String> result = sut.delete("/to-delete", String.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo("xx");
        assertHttpPathAndVerb("/to-delete", "DELETE");
    }
}
package org.gbl.out.http;

import com.google.gson.reflect.TypeToken;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;
import org.gbl.out.ContactsGateway;
import org.gbl.out.ContactResponse;
import org.gbl.utils.JSON;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HttpContactGateway implements ContactsGateway {

    private static final List<Integer> OK_RESPONSES = List.of(200, 201, 204);
    private static final String RESOURCE = "/contacts";

    private final HttpClient client;
    private final String baseUrl;

    public HttpContactGateway(HttpClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
    }

    private HttpRequest.Builder baseRequest() {
        return HttpRequest.newBuilder()
                .version(Version.HTTP_2)
                .timeout(Duration.of(500, ChronoUnit.MILLIS))
                .uri(URI.create(baseUrl + RESOURCE))
                .headers("Accept", "application/json");
    }

    @Override
    public ContactResponse create(CreateContactRequest request) {
        final var httpRequest = baseRequest()
                .POST(BodyPublishers.ofString(JSON.stringify(request)))
                .build();
        return execute(httpRequest);
    }

    @Override
    public ContactResponse get(String contactId) {
        final var httpRequest = baseRequest()
                .GET()
                .uri(URI.create(baseUrl + RESOURCE + "/" + contactId))
                .build();
        return execute(httpRequest);
    }

    @Override
    public ContactResponse update(UpdateContactRequest request) {
        final var httpRequest = baseRequest()
                .PUT(BodyPublishers.ofString(JSON.stringify(request)))
                .build();
        return execute(httpRequest);
    }

    @Override
    public void delete(String contactId) {
        final var httpRequest = baseRequest()
                .uri(URI.create(baseUrl + RESOURCE + "/" + contactId))
                .DELETE()
                .build();
        execute(httpRequest);
    }

    private ContactResponse execute(HttpRequest httpRequest) {
        try {
            final var response = client.send(httpRequest, BodyHandlers.ofString());
            var type = new TypeToken<ApiResponse<ContactResponse>>() {}.getType();
            ApiResponse<ContactResponse> apiResponse = JSON.parse(response.body(), type);
            if (!OK_RESPONSES.contains(response.statusCode())) {
                throw new RuntimeException(apiResponse.message());
            }
            return apiResponse.data();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during request", e);
        }
    }
}

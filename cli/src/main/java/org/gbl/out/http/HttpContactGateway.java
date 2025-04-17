package org.gbl.out.http;

import com.google.gson.reflect.TypeToken;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.out.ContactsGateway;
import org.gbl.out.CreateContactResponse;
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

public class HttpContactGateway implements ContactsGateway {

    private static final String resource = "/contacts";
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
                .uri(URI.create(baseUrl + resource))
                .headers("Accept", "application/json");
    }

    @Override
    public CreateContactResponse create(CreateContactRequest request) {
        try {
            final var httpRequest = baseRequest()
                    .POST(BodyPublishers.ofString(JSON.stringify(request)))
                    .build();
            final var response = client.send(httpRequest, BodyHandlers.ofString());
            var type = new TypeToken<ApiResponse<CreateContactResponse>>() {}.getType();
            ApiResponse<CreateContactResponse> apiResponse = JSON.parse(response.body(), type);
            if (response.statusCode() != 201) {
                throw new RuntimeException(apiResponse.message());
            }
            return apiResponse.data();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during request", e);
        }
    }
}

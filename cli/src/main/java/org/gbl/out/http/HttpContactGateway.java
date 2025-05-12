package org.gbl.out.http;

import com.google.gson.reflect.TypeToken;
import io.vavr.control.Try;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.service.json.JsonParser;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.in.UpdateContact.UpdateContactRequest;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpContactGateway implements ContactsGateway {

    private static final List<Integer> OK_RESPONSES = List.of(200, 201, 204);
    private static final String RESOURCE = "/contacts";

    private static final Type CONTACT_RESPONSE_TYPE = ContactResponse.class;
    private static final Type PAGINATION_RESPONSE_TYPE =
            TypeToken.getParameterized(Pagination.class, ContactResponse.class).getType();

    private final JsonParser jsonParser;
    private final HttpClient client;
    private final String baseUrl;


    public HttpContactGateway(JsonParser jsonParser, HttpClient client, String baseUrl) {
        this.jsonParser = jsonParser;
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
    public Try<ContactResponse> create(CreateContactRequest request) {
        final var httpRequest = baseRequest()
                .POST(BodyPublishers.ofString(jsonParser.stringify(request)))
                .build();
        return Try.of(() -> (ContactResponse) execute(httpRequest, CONTACT_RESPONSE_TYPE).orElseThrow());
    }

    @Override
    public Try<ContactResponse> get(String contactId) {
        final var httpRequest = baseRequest()
                .GET()
                .uri(URI.create(baseUrl + RESOURCE + "/" + contactId))
                .build();
        return Try.of(() -> (ContactResponse) execute(httpRequest, CONTACT_RESPONSE_TYPE).orElseThrow());
    }

    @Override
    public Try<Void> update(UpdateContactRequest request) {
        final var httpRequest = baseRequest()
                .uri(URI.create(baseUrl + RESOURCE + "/" + request.id))
                .PUT(BodyPublishers.ofString(jsonParser.stringify(request)))
                .build();
        return Try.run(() -> execute(httpRequest, CONTACT_RESPONSE_TYPE));
    }

    @Override
    public Try<Void> delete(String contactId) {
        final var httpRequest = baseRequest()
                .uri(URI.create(baseUrl + RESOURCE + "/" + contactId))
                .DELETE()
                .build();
        return Try.run(() -> execute(httpRequest, CONTACT_RESPONSE_TYPE));
    }

    @Override
    public Try<Pagination<ContactResponse>> search(SearchRequest<ContactFilter> searchRequest) {
        final var httpRequest = baseRequest()
                .uri(URI.create(baseUrl + RESOURCE + "?" + toString(searchRequest)))
                .GET()
                .build();
        return Try.of(
                () -> (Pagination<ContactResponse>) execute(httpRequest, PAGINATION_RESPONSE_TYPE)
                        .orElseThrow());
    }

    private String toString(SearchRequest<ContactFilter> searchRequest) {
        final var paginationParams = List.of("page=" + searchRequest.page(),
                                             "size=" + searchRequest.size(),
                                             "order=" + searchRequest.order());
        var params = new ArrayList<>(paginationParams);
        return String.join("&", params);
    }

    private <T> Optional<T> execute(HttpRequest httpRequest, Type type) {
        try {
            final var response = client.send(httpRequest, BodyHandlers.ofString());
            ApiResponse<T> apiResponse = jsonParser.parse(response.body(), toParameterizedOf(type));
            if (!OK_RESPONSES.contains(response.statusCode())) {
                throw new HttpApiException(apiResponse.message());
            }
            return Optional.ofNullable(apiResponse).map(ApiResponse::data);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error: request failed", e);
        }
    }

    private static Type toParameterizedOf(Type type) {
        return TypeToken.getParameterized(ApiResponse.class, type).getType();
    }
}

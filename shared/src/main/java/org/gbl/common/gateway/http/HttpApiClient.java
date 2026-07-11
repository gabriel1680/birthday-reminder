package org.gbl.common.gateway.http;

import com.google.gson.reflect.TypeToken;
import io.vavr.control.Try;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.common.service.json.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;

public class HttpApiClient {

    private final JsonParser jsonParser;
    private final HttpClient client;
    private final String baseUrl;

    public HttpApiClient(JsonParser jsonParser, HttpClient client, String baseUrl) {
        this.jsonParser = jsonParser;
        this.client = client;
        this.baseUrl = baseUrl;
    }

    public <T> Try<T> get(String path, Type responseType) {
        return get(path, responseType, emptyMap());
    }

    public <T> Try<T> get(String path, Type responseType, Map<String, String> headers) {
        final var variadicHeaders = headers.entrySet()
                .stream()
                .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                .toArray(String[]::new);
        final var getRequestBuilder = baseRequest(path).GET();
        if (variadicHeaders.length > 0) getRequestBuilder.headers(variadicHeaders);
        return execute(getRequestBuilder.build(), responseType);
    }

    public <T> Try<T> post(String path, Object body, Type responseType) {
        final var httpRequest = baseRequest(path)
                .POST(BodyPublishers.ofString(jsonParser.stringify(body)))
                .build();
        return execute(httpRequest, responseType);
    }

    public <T> Try<T> put(String path, Object body, Type responseType) {
        final var httpRequest = baseRequest(path)
                .PUT(BodyPublishers.ofString(jsonParser.stringify(body)))
                .build();
        return execute(httpRequest, responseType);
    }

    public <T> Try<T> delete(String path, Type responseType) {
        final var httpRequest = baseRequest(path)
                .DELETE()
                .build();
        return execute(httpRequest, responseType);
    }

    private Builder baseRequest(String path) {
        return HttpRequest.newBuilder()
                .version(Version.HTTP_2)
                .timeout(Duration.of(500, ChronoUnit.MILLIS))
                .uri(URI.create(baseUrl + path))
                .headers("Accept", "application/json");
    }

    private <T> Try<T> execute(HttpRequest httpRequest, Type type) {
        return Try.of(() -> {
            try {
                final var response = client.send(httpRequest, BodyHandlers.ofString());
                final ApiResponse<T> apiResponse = jsonParser.parse(response.body(), toParameterizedOf(type));
                return switch (response.statusCode()) {
                    case 200, 201, 204 -> apiResponse.data();
                    case 404 -> throw new ResourceNotFoundException(apiResponse.message());
                    default -> throw new HttpApiException(apiResponse.message());

                };
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error: request failed", e);
            }
        });
    }

    private static Type toParameterizedOf(Type type) {
        return TypeToken.getParameterized(ApiResponse.class, type).getType();
    }
}

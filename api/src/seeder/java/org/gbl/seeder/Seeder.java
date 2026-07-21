package org.gbl.seeder;

import com.google.gson.Gson;
import net.datafaker.Faker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class Seeder {

    protected final Gson gson;
    protected final Faker faker;
    protected final HttpClient httpClient;
    protected final String apiBaseUrl;

    protected Seeder(String apiBaseUrl, Gson gson, Faker faker, HttpClient httpClient) {
        this.apiBaseUrl = stripTrailingSlash(apiBaseUrl);
        this.gson = gson;
        this.faker = faker;
        this.httpClient = httpClient;
    }

    public abstract void seed(int count) throws IOException, InterruptedException;

    protected void ensureApiIsAvailable(String endpoint) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder(URI.create(apiBaseUrl + endpoint)).GET().build();
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("The API is not available at " + apiBaseUrl + endpoint);
        }
    }

    protected void post(String endpoint, Object payload) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder(URI.create(apiBaseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload)))
                .build();
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Request failed with status %d: %s"
                    .formatted(response.statusCode(), response.body()));
        }
    }

    private static String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}

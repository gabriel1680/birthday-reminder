package org.gbl;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class APITest {

    private final Javalin app = API.create().getServer();

    private final String validBody = "{\"name\":\"Mario Kart\", \"birthdate\":\"2018-11-15\"}";

    @Test
    void should_create_a_contact() {
        JavalinTest.test(app, (server, httpClient) -> {
            final var response = httpClient.post("/contacts", validBody);
            assertThat(response.header("Content-Type")).isEqualTo("application/json");
            assertThat(response.code()).isEqualTo(201);
        });
    }

    @Test
    void should_return_400_when_payload_is_invalid() {
        JavalinTest.test(app, (server, httpClient) -> {
            final var body = "{\"name\":\"\"}";
            final var response = httpClient.post("/contacts", body);
            assertThat(response.header("Content-Type")).isEqualTo("application/json");
            assertThat(response.code()).isEqualTo(400);
        });
    }

    @Test
    void should_return_422_when_business_error() {
        JavalinTest.test(app, (server, httpClient) -> {
            httpClient.post("/contacts", validBody);
            final var response = httpClient.post("/contacts", validBody);
            assertThat(response.header("Content-Type")).isEqualTo("application/json");
            assertThat(response.code()).isEqualTo(422);
        });
    }
}
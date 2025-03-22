package org.gbl;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class APITest {

    private static final String VALID_BODY =
            "{\"name\":\"Mario Kart\", \"birthdate\":\"2018-11-15\"}";

    private final Javalin app = API.create().getServer();

    @Nested
    class PostContactsShould {

        @Test
        void return_201_when_success() {
            JavalinTest.test(app, (server, httpClient) -> {
                final var response = httpClient.post("/contacts", VALID_BODY);
                assertThat(response.header("Content-Type")).isEqualTo("application/json");
                assertThat(response.code()).isEqualTo(201);
            });
        }

        @Test
        void return_400_when_payload_is_invalid() {
            JavalinTest.test(app, (server, httpClient) -> {
                final var body = "{\"name\":\"\"}";
                final var response = httpClient.post("/contacts", body);
                assertThat(response.header("Content-Type")).isEqualTo("application/json");
                assertThat(response.code()).isEqualTo(400);
            });
        }

        @Test
        void return_422_when_business_error() {
            JavalinTest.test(app, (server, httpClient) -> {
                httpClient.post("/contacts", VALID_BODY);
                final var response = httpClient.post("/contacts", VALID_BODY);
                assertThat(response.header("Content-Type")).isEqualTo("application/json");
                assertThat(response.code()).isEqualTo(422);
            });
        }
    }
}
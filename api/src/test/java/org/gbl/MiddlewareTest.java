package org.gbl;

import org.gbl.common.service.json.JsonService;
import org.gbl.contacts.application.service.query.InvalidSearchInputException;
import org.gbl.contacts.application.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import org.gbl.controller.common.HttpAPIResponse;
import org.gbl.controller.common.InvalidPayloadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.controller.common.ResponseStatus.ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MiddlewareTest extends SparkControllerTest {

    @Mock
    private JsonService jsonService;

    @InjectMocks
    private Middleware sut;

    @Test
    void should_set_content_type_as_json() {
        Middleware.json(response);
        verify(response).type("application/json");
    }

    @Test
    void should_set_headers_to_enable_cors() {
        Middleware.enableCORS(response);
        verify(response).header("Access-Control-Allow-Origin", "*");
        verify(response).header("Access-Control-Allow-Headers", "*");
        verify(response).header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, " +
                "OPTIONS");
    }

    @Captor
    private ArgumentCaptor<HttpAPIResponse> apiResponseCaptor;

    @ParameterizedTest
    @MethodSource("exceptionsProvider")
    void should_handle_exceptions(int statusCode, Exception exception, String message) {
        when(jsonService.stringify(any())).thenReturn("a json body");
        sut.handleException(exception, request, response);
        verify(response).status(statusCode);
        verify(response).body("a json body");
        verify(jsonService).stringify(apiResponseCaptor.capture());
        assertThat(apiResponseCaptor.getValue())
                .isInstanceOf(HttpAPIResponse.class)
                .extracting(HttpAPIResponse::status, HttpAPIResponse::message)
                .containsExactly(ERROR, message);
    }

    private static Stream<Arguments> exceptionsProvider() {
        return Stream.of(
                Arguments.of(500, new Exception("avocado"), "Internal server error"),
                Arguments.of(400, new InvalidPayloadException("strawberry"), "strawberry"),
                Arguments.of(400, new InvalidSearchInputException("peanut"), "peanut"),
                Arguments.of(400, new IllegalArgumentException("coconut"), "coconut"),
                Arguments.of(404, new ContactNotFoundException("crumbleberry"), "Contact with id \"crumbleberry\" not found"),
                Arguments.of(422, new ContactAlreadyExistsException("blueberry"), "Contact already exists with name \"blueberry\"")
        );
    }
}
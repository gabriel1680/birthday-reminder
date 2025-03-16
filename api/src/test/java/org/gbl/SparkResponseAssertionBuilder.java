package org.gbl;

import org.eclipse.jetty.http.HttpStatus.Code;
import org.json.JSONObject;
import spark.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class SparkResponseAssertionBuilder {

    private String type = "application/json";
    private Code statusCode;
    private HttpAPIResponse expected;
    private HttpAPIResponse actual;

    private final Response response;

    private SparkResponseAssertionBuilder(Response response) {
        this.response = response;
    }

    public static SparkResponseAssertionBuilder aAssertionFor(Response response) {
        return new SparkResponseAssertionBuilder(response);
    }

    public SparkResponseAssertionBuilder withStatusCode(Code statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public SparkResponseAssertionBuilder forExpected(ResponseStatus status,
                                                     String message, JSONObject data) {
        this.expected = new HttpAPIResponse(status, message, data);
        return this;
    }

    public SparkResponseAssertionBuilder withActual(HttpAPIResponse actual) {
        this.actual = actual;
        return this;
    }

    public void build() {
        verify(response).type(type);
        verify(response).status(statusCode.getCode());
        assertThat(actual.status()).isEqualTo(expected.status());
        assertThat(actual.message()).isEqualTo(expected.message());
        assertThat(actual.data()).usingRecursiveComparison().isEqualTo(expected.data());
    }
}

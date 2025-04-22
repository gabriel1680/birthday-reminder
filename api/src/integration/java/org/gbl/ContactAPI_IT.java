package org.gbl;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ContactAPI_IT extends IntegrationTest {

    private static final String RESOURCE_URL = BASE_URL + "/contacts";

    @Test
    void given_a_new_contact_should_create_it() {
        given()
                .body(withJson("Carl Edward Sagan", "1934-11-09T00:00:00Z"))
                .when()
                .post(RESOURCE_URL)
                .then()
                .statusCode(201);
    }

    private static String withJson(String name, String birthdate) {
        return new JSONObject()
                .put("name", name)
                .put("birthdate", birthdate)
                .toString();
    }
}

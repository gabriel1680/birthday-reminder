package org.gbl;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class ContactAPI_IT extends IntegrationTest {

    private static final String RESOURCE_URL = BASE_URL + "/contacts";

    @Test
    void given_a_new_contact_should_create_it() {
        given()
                .body(withJson("Carl Edward Sagan", "1934-11-09T00:00:00Z"))
        .when()
                .post(RESOURCE_URL)
        .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("status", equalTo("success"))
                .body("message", emptyString())
                .body("data", notNullValue())
                .body("data.id", notNullValue())
                .body("data.name", stringContainsInOrder("Carl"))
                .body("data.birthdate", is("1934-11-09"));
    }

    @Test
    void given_a_contact_should_retrieve_it() {
        var response = given()
                .body(withJson("Isaac Newton", "1643-01-04T00:00:00Z"))
                .when()
                .post(RESOURCE_URL)
                .andReturn();
        var id = new JSONObject(response.body().print()).getJSONObject("data").getString("id");
        when()
                .get(RESOURCE_URL + "/" + id)
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("success"))
                .body("message", emptyString())
                .body("data", notNullValue())
                .body("data.id", equalTo(id))
                .body("data.name", stringContainsInOrder("Isaac"))
                .body("data.birthdate", is("1643-01-04"));
        ;
    }

    private static String withJson(String name, String birthdate) {
        return new JSONObject()
                .put("name", name)
                .put("birthdate", birthdate)
                .toString();
    }
}

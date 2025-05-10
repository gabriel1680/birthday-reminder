package org.gbl;

import io.restassured.http.ContentType;
import org.gbl.dsl.BirthdayReminderDSL;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BReminderAPI_IT extends IntegrationTest {

    @Test
    void notFound() {
        given()
                .body(withJson("Carl Edward Sagan", "1934-11-09T00:00:00Z"))
        .when()
                .post(BirthdayReminderDSL.BASE_URL + "/not-found-url")
        .then()
                .statusCode(404)
                .contentType(ContentType.TEXT);
    }
}

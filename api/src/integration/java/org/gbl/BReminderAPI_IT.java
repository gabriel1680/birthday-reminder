package org.gbl;

import io.restassured.http.ContentType;
import org.gbl.dsl.BirthdayReminderDSL;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class BReminderAPI_IT {

    @Test
    void notFound() {
        when()
                .post(BirthdayReminderDSL.BASE_URL + "/not-found-url")
        .then()
                .statusCode(404)
                .contentType(ContentType.TEXT);
    }
}

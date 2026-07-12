package org.gbl;

import io.restassured.http.ContentType;
import org.gbl.dsl.BirthdayReminderDSL;
import org.gbl.dsl.ContactDSL.ITContact;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.gbl.dsl.ContactDSL.ITContactBuilder.aContact;
import static org.gbl.dsl.ContactDSL.RESOURCE_URL;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class ContactBReminderAPI_IT {

    @Nested
    class  GivenNoContacts {

        @Test
        void should_create_it() {
            final var CARL_SAGAN = aContact()
                    .withName("Carl Edward Sagan")
                    .withBirthdate("1934-11-09T00:00:00Z")
                    .build();
            final String userId = given()
                    .body(withJSON(CARL_SAGAN.name(), CARL_SAGAN.birthdate()))
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
                    .body("data.birthdate", is("1934-11-09"))
                    .extract().body().jsonPath().get("data.id");
            BirthdayReminderDSL.remove(new ITContact(userId, CARL_SAGAN.name(), CARL_SAGAN.birthdate()));
        }
    }

    @Nested
    class GivenAExistingContact {

        private ITContact ISAAC_NEWTON = aContact()
                .withName("Isaac Newton")
                .withBirthdate("1643-01-04T00:00:00Z")
                .build();

        private ITContact DANIEL_BERNOULLI = aContact()
                .withName("Daniel Bernoulli")
                .withBirthdate("1700-02-08T00:00:00Z")
                .build();

        @BeforeEach
        void setUp() {
            ISAAC_NEWTON = BirthdayReminderDSL.register(ISAAC_NEWTON);
        }

        @AfterEach
        void tearDown() {
            BirthdayReminderDSL.remove(ISAAC_NEWTON);
        }

        @Test
        void should_retrieve_it() {
            when()
                    .get(RESOURCE_URL + "/" + ISAAC_NEWTON.id())
            .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("status", equalTo("success"))
                    .body("message", emptyString())
                    .body("data", notNullValue())
                    .body("data.id", equalTo(ISAAC_NEWTON.id()))
                    .body("data.name", stringContainsInOrder("Isaac"))
                    .body("data.birthdate", is("1643-01-04"));
        }

        @Test
        void should_update_it() {
            given()
                    .body(withJSON("Albert Einstein", "1879-03-14T00:00:00Z"))
            .when()
                    .put(RESOURCE_URL + "/" + ISAAC_NEWTON.id())
            .then()
                    .statusCode(204)
                    .contentType(ContentType.JSON)
                    .body(emptyString());
        }

        @Test
        void should_delete_it() {
            DANIEL_BERNOULLI = BirthdayReminderDSL.register(DANIEL_BERNOULLI);
            when()
                    .delete(RESOURCE_URL + "/" + DANIEL_BERNOULLI.id())
            .then()
                    .statusCode(204)
                    .contentType(ContentType.JSON)
                    .body(emptyString());
        }

        @Test
        void should_retrieve_all() {
            when()
                    .get(RESOURCE_URL + "?page=1&size=15")
            .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("status", equalTo("success"))
                    .body("message", emptyString())
                    .body("data", notNullValue())
                    .body("data.current_page", is(1))
                    .body("data.size", is(15))
                    .body("data.total", is(1))
                    .body("data.last_page", is(1))
                    .body("data.values", hasSize(1));
        }
    }

    @Nested
    class GivenABirthdayAfterToday {

        private final String now2000Plus10DaysString = OffsetDateTime.now()
                .withYear(2000)
                .plusDays(10)
                .toInstant()
                .toString();

        private ITContact JOHN_DOE = aContact()
                .withName("John Doe")
                .withBirthdate(now2000Plus10DaysString)
                .build();

        @BeforeEach
        void setUp() {
            JOHN_DOE = BirthdayReminderDSL.register(JOHN_DOE);
        }

        @AfterEach
        void tearDown() {
            BirthdayReminderDSL.remove(JOHN_DOE);
        }

        @Test
        void should_get_upcoming_birthdays() {
            given()
                    .headers("X-Time-Zone", "America/Sao_Paulo")
            .when()
                    .get(RESOURCE_URL + "/upcoming-birthdays")
            .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("status", equalTo("success"))
                    .body("message", emptyString())
                    .body("data", hasSize(1));
        }
    }

    private static String withJSON(String name, String birthdate) {
        return new JSONObject()
                .put("name", name)
                .put("birthdate", birthdate)
                .toString();
    }
}

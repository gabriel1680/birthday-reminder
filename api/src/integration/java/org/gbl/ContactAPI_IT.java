package org.gbl;

import io.restassured.http.ContentType;
import org.gbl.dsl.BirthdayReminderDSL;
import org.gbl.dsl.ContactDSL;
import org.gbl.dsl.ContactDSL.ITContact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.gbl.dsl.ContactDSL.ITContactBuilder.aContact;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class ContactAPI_IT extends IntegrationTest {

    @Test
    void given_a_new_contact_should_create_it() {
        given()
            .body(withJson("Carl Edward Sagan", "1934-11-09T00:00:00Z"))
        .when()
            .post(ContactDSL.RESOURCE_URL)
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
                .get(ContactDSL.RESOURCE_URL + "/" + ISAAC_NEWTON.id())
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
                .body(withJson("Albert Einstein", "1879-03-14T00:00:00Z"))
            .when()
                .put(ContactDSL.RESOURCE_URL + "/" + ISAAC_NEWTON.id())
            .then()
                .statusCode(204)
                .contentType(ContentType.JSON)
                .body(emptyString());
        }

        @Test
        void given_a_contact_should_delete_it() {
            DANIEL_BERNOULLI = BirthdayReminderDSL.register(DANIEL_BERNOULLI);
            when()
                .delete(ContactDSL.RESOURCE_URL + "/" + DANIEL_BERNOULLI.id())
            .then()
                .statusCode(204)
                .contentType(ContentType.JSON)
                .body(emptyString());
        }
    }
}

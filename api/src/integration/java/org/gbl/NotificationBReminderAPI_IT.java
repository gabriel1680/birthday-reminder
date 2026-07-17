package org.gbl;

import io.restassured.http.ContentType;
import org.gbl.dsl.BirthdayReminderDSL;
import org.gbl.dsl.ContactDSL.ITContact;
import org.gbl.dsl.NotificationDSL.ITNotification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.gbl.dsl.NotificationDSL.ITNotificationBuilder.aNotification;
import static org.gbl.dsl.NotificationDSL.RESOURCE_URL;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class NotificationBReminderAPI_IT {

    @Nested
    class GivenNoNotifications {

        @Test
        void should_create_it() {
            final var SLACK = aNotification()
                    .withType("slack")
                    .withValue("api@slack.com")
                    .build();
            given()
                    .body(withJSON(SLACK.type(), SLACK.value()))
            .when()
                    .post(RESOURCE_URL)
            .then()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .body("status", equalTo("success"))
                    .body("message", emptyString())
                    .body("data", notNullValue())
                    .body("data.id", notNullValue())
                    .body("data.type", stringContainsInOrder(SLACK.type()))
                    .body("data.value", is(SLACK.value()));
        }
    }

    @Nested
    class GivenAExistingContact {

        private ITNotification EMAIL = aNotification()
                .withType("e-mail")
                .withValue("john.doe@gmail.com")
                .build();

        private ITNotification SMS = aNotification()
                .withType("SMS")
                .withValue("+1 8484956791")
                .build();

        @BeforeEach
        void setUp() {
            EMAIL = BirthdayReminderDSL.register(EMAIL);
        }

        @AfterEach
        void tearDown() {
            BirthdayReminderDSL.remove(EMAIL);
        }

        @Test
        void should_retrieve_it() {
            when()
                    .get(RESOURCE_URL + "/" + EMAIL.id())
            .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("status", equalTo("success"))
                    .body("message", emptyString())
                    .body("data", notNullValue())
                    .body("data.id", equalTo(EMAIL.id()))
                    .body("data.type", stringContainsInOrder("e-mail"))
                    .body("data.value", is("john.doe@gmail.com"));
        }

        @Test
        void should_not_update_it() {
            given()
                    .body(withJSON("slack", "random@slack.com"))
            .when()
                    .put(RESOURCE_URL + "/" + EMAIL.id())
            .then()
                    .statusCode(404);
        }

        @Test
        void given_a_contact_should_delete_it() {
            SMS = BirthdayReminderDSL.register(SMS);
            when()
                    .delete(RESOURCE_URL + "/" + SMS.id())
            .then()
                    .statusCode(204)
                    .contentType(ContentType.JSON)
                    .body(emptyString());
        }

        @Test
        void given_two_contacts_should_retrieve_all() {
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
                    .body("data.total", is(2))
                    .body("data.last_page", is(1))
                    .body("data.values", hasSize(2));
        }
    }

    private static String withJSON(String type, String value) {
        return new JSONObject()
                .put("type", type)
                .put("value", value)
                .toString();
    }
}

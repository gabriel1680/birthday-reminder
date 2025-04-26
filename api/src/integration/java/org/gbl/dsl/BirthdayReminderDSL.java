package org.gbl.dsl;

import org.gbl.dsl.ContactDSL.ITContact;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;

public class BirthdayReminderDSL {

    public static final String BASE_URL = "http://localhost:8080";

    public static ITContact register(ITContact aContact) {
        var response = given()
                .body(toJson(aContact))
                .when()
                .post(BASE_URL + ContactDSL.RESOURCE)
                .andReturn();
        final var json = new JSONObject(response.body().print()).getJSONObject("data");
        return fromJson(json);
    }

    public static void remove(ITContact aContact) {
        when()
                .delete(BASE_URL + ContactDSL.RESOURCE + "/" + aContact.id())
                .then()
                .statusCode(NO_CONTENT.getCode());
    }

    private static String toJson(ITContact aContact) {
        return new JSONObject()
                .put("name", aContact.name())
                .put("birthdate", aContact.birthdate())
                .toString();
    }

    private static ITContact fromJson(JSONObject jsonObject) {
        return new ITContact(jsonObject.getString("id"), jsonObject.getString("name"),
                             jsonObject.getString("birthdate"));
    }
}

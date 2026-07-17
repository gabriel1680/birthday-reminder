package org.gbl.dsl;

import io.restassured.response.Response;
import org.gbl.dsl.ContactDSL.ITContact;
import org.gbl.dsl.NotificationDSL.ITNotification;
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
        return contactFrom(getJsonDataFrom(response));
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

    private static ITContact contactFrom(JSONObject jsonObject) {
        return new ITContact(jsonObject.getString("id"), jsonObject.getString("name"),
                             jsonObject.getString("birthdate"));
    }

    public static ITNotification register(ITNotification aNotification) {
        var response = given()
                .body(toJson(aNotification))
                .when()
                .post(BASE_URL + NotificationDSL.RESOURCE)
                .andReturn();
        return notificationFrom(getJsonDataFrom(response));
    }

    private static JSONObject getJsonDataFrom(Response response) {
        return new JSONObject(response.body().print()).getJSONObject("data");
    }

    private static ITNotification notificationFrom(JSONObject jsonObject) {
        return new ITNotification(
                jsonObject.getString("id"),
                jsonObject.getString("type"),
                jsonObject.getString("value")
        );
    }

    private static String toJson(ITNotification aNotification) {
        return new JSONObject()
                .put("type", aNotification.type())
                .put("value", aNotification.value())
                .toString();
    }

    public static void remove(ITNotification aNotification) {
        when()
                .delete(BASE_URL + ContactDSL.RESOURCE + "/" + aNotification.id())
                .then()
                .statusCode(NO_CONTENT.getCode());
    }
}

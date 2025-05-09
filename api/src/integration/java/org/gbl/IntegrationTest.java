package org.gbl;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class IntegrationTest {

    private static BReminderAPI api;

    @BeforeAll
    public static void init() {
        api = new BReminderAPI();
        api.start();
        api.awaitInitialization();
    }

    @AfterAll
    public static void tearDown() {
        api.stop();
    }

    protected static String withJson(String name, String birthdate) {
        return new JSONObject()
                .put("name", name)
                .put("birthdate", birthdate)
                .toString();
    }
}

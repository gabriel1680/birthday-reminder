package org.gbl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class IntegrationTest {

    private static API api;

    protected static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    public static void init() {
        api = new API();
        api.start();
        api.awaitInitialization();
    }

    @AfterAll
    public static void tearDown() {
        api.stop();
    }
}

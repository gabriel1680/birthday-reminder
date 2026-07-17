package org.gbl;

import org.gbl.common.service.json.Json;
import spark.Spark;

import static spark.Spark.port;

public class BReminderAPI {

    private final Routes routes;
    private final Middleware middleware;

    public BReminderAPI(Json json) {
        this.middleware = new Middleware(json);
        this.routes = new Routes();
    }

    public void start() {
        port(getPort());
        routes.init();
        middleware.init();
    }

    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() {
        Spark.stop();
    }

    private static int getPort() {
        var processBuilder = new ProcessBuilder();
        var port = processBuilder.environment().getOrDefault("PORT", "8080");
        return Integer.parseInt(port);
    }
}

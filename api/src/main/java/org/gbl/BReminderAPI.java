package org.gbl;

import org.gbl.common.service.json.JsonService;
import spark.Spark;

import static spark.Spark.port;

public class BReminderAPI {

    private final Routes routes;
    private final Middleware middleware;

    public BReminderAPI(JsonService jsonService) {
        this.middleware = new Middleware(jsonService);
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
        final var processBuilder = new ProcessBuilder();
        final var port = processBuilder.environment().getOrDefault("PORT", "8080");
        return Integer.parseInt(port);
    }
}

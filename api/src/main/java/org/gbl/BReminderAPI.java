package org.gbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static spark.Spark.before;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.port;

public class BReminderAPI {

    private static final Logger logger = LoggerFactory.getLogger(BReminderAPI.class);

    private final APIRoutes routes;

    public BReminderAPI() {
        routes = new APIRoutes();
    }

    public void start() {
        port(getPort());
        enableCORS();
        configureLogging();
        routes.init();
        configureInternalServerError();
        configureNotImplemented();
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

    private void configureInternalServerError() {
        internalServerError((req, res) -> {
            res.status(INTERNAL_SERVER_ERROR_500);
            logger.error("Internal Server Error: {}", req.pathInfo());
            return "Internal Server Error.";
        });
    }

    private void configureNotImplemented() {
        notFound((req, res) -> {
            res.status(NOT_FOUND_404);
            logger.error("Not found: {}", req.pathInfo());
            return "Not Found.";
        });
    }

    private void enableCORS() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, " +
                    "OPTIONS");
        });
    }

    private void configureLogging() {
        before((request, response) -> logger.info("Request: {} {} - headers: {}",
                                                  request.requestMethod(),
                                                  request.uri(),
                                                  request.headers()));
    }
}

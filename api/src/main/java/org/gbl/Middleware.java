package org.gbl;

import org.gbl.common.service.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.function.BiFunction;

import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.gbl.controller.common.ExceptionMapper.mapException;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

class Middleware {

    private static final Logger logger = LoggerFactory.getLogger(Middleware.class);

    private static final BiFunction<Request, Response, String> ERR_505_HANDLER =
            (req, res) -> error(res, INTERNAL_SERVER_ERROR_500, "Internal Server Error: {}", req,
                                "Internal Server Error.");

    private static final BiFunction<Request, Response, String> ERR_404_HANDLER =
            (req, res) -> error(res, NOT_FOUND_404, "Not found: {}", req, "Not Found.");

    private final Json json;

    public Middleware(Json json) {
        this.json = json;
    }

    public void init() {
        internalServerError((req, res) -> ERR_505_HANDLER);
        notFound((req, res) -> ERR_404_HANDLER);
        exception(Exception.class, this::handleException);
        before((request, response) -> {
            enableCORS(response);
            logInfo(request);
            json(response);
        });
    }

    public static void logInfo(Request request) {
        logger.info("Request: {} {} - headers: {}",
                    request.requestMethod(),
                    request.uri(),
                    request.headers());
    }

    public void handleException(Exception exception, Request request, Response response) {
        final var httpApiResponse = mapException(response, exception);
        response.body(json.stringify(httpApiResponse));
    }

    public static void enableCORS(Response response) {
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Headers", "*");
        response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, " +
                "OPTIONS");
    }

    public static void json(Response response) {
        response.type("application/json");
    }

    private static String error(Response res, int status, String logMessage, Request req,
                                String userMessage) {
        res.type("text/plain");
        res.status(status);
        logger.error(logMessage, req.pathInfo());
        return userMessage;
    }
}

package org.gbl.controller;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    public static void internalServerErrorPage(Context context) {
        context.render("internal-server-error-page.jte");
    }

    public static void notFoundPage(Context context) {
        context.render("not-found-page.jte");
    }
}

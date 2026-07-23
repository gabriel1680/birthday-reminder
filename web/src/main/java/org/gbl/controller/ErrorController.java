package org.gbl.controller;

import io.javalin.http.Context;

import static org.gbl.config.JTEPages.INTERNAL_SERVER_ERROR;
import static org.gbl.config.JTEPages.NOT_FOUND;

public class ErrorController {

    public static void internalServerErrorPage(Context context) {
        context.render(INTERNAL_SERVER_ERROR);
    }

    public static void notFoundPage(Context context) {
        context.render(NOT_FOUND);
    }
}

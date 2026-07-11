package org.gbl.controller;

import io.javalin.http.Context;

public class ErrorController {

    public static void internalServerErrorPage(Context context) {
        context.render("internal-server-error-page.jte");
    }

    public static void notFoundPage(Context context) {
        context.render("not-found-page.jte");
    }
}

package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.ErrorController;
import org.gbl.controller.SearchContactsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web {

    private static final Logger LOGGER = LoggerFactory.getLogger(Web.class);

    private final Javalin server;
    private final SearchContactsController searchContactsController;
    private final ContactDetailsController contactDetailsController;

    public Web(
        SearchContactsController searchContactsController,
        ContactDetailsController contactDetailsController
    ) {
        this.searchContactsController = searchContactsController;
        this.contactDetailsController = contactDetailsController;
        server = Javalin.create(Web::configureServer);
        initRoutes();
    }

    private void initRoutes() {
        server.get("/", searchContactsController::searchPage);
        server.get("/details/{id}", contactDetailsController::contactInfo);
        server.error(404, ErrorController::notFoundPage);
        server.error(500, ErrorController::internalServerErrorPage);
        server.exception(RuntimeException.class, (e, context) -> {
            LOGGER.error("Internal Server Error", e);
            context.status(500);
        });
    }

    private static void configureServer(JavalinConfig config) {
        config.fileRenderer(new JavalinJte());
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.hostedPath = "/";
            staticFileConfig.directory = "/public";
            staticFileConfig.mimeTypes.add("text/css", ".css");
        });
    }

    public Javalin getServer() {
        return server;
    }

    public void start(int port) {
        server.start(port);
    }
}

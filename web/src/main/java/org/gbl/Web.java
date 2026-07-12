package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.ErrorController;
import org.gbl.controller.NotificationsController;
import org.gbl.controller.SearchContactsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web {

    private static final Logger LOGGER = LoggerFactory.getLogger(Web.class);

    private final Javalin server;
    private final SearchContactsController searchContactsController;
    private final ContactDetailsController contactDetailsController;
    private final NotificationsController notificationsController;

    public Web(
            SearchContactsController searchContactsController,
            ContactDetailsController contactDetailsController,
            NotificationsController notificationsController
    ) {
        this.searchContactsController = searchContactsController;
        this.contactDetailsController = contactDetailsController;
        this.notificationsController = notificationsController;
        server = Javalin.create(Web::configureServer);
        initRoutes();
    }

    private void initRoutes() {
        server.get("/", searchContactsController::searchPage);
        server.get("/details/{id}", contactDetailsController::contactInfo);
        server.get("/notifications", notificationsController::notificationPage);
        server.error(404, ErrorController::notFoundPage);
        server.error(500, ErrorController::internalServerErrorPage);
        server.exception(RuntimeException.class, (e, context) -> {
            LOGGER.error("Internal Server Error", e);
            context.status(500);
        });
        server.exception(ResourceNotFoundException.class, (e, context) -> {
            LOGGER.warn("Contact Not Found", e);
            context.status(404);
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

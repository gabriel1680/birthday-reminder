package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.controller.ContactsController;
import org.gbl.controller.ErrorController;
import org.gbl.controller.HomeController;
import org.gbl.controller.SearchContactsController;
import org.gbl.controller.notifications.NotificationsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web {

    private static final Logger LOGGER = LoggerFactory.getLogger(Web.class);

    private final Javalin server;
    private final HomeController homeController;
    private final SearchContactsController searchContactsController;
    private final ContactsController contactsController;
    private final NotificationsController notificationsController;

    public Web(
            HomeController homeController,
            SearchContactsController searchContactsController,
            ContactsController contactsController,
            NotificationsController notificationsController
    ) {
        this.homeController = homeController;
        this.searchContactsController = searchContactsController;
        this.contactsController = contactsController;
        this.notificationsController = notificationsController;
        server = Javalin.create(Web::configureServer);
        initRoutes();
    }

    private void initRoutes() {
        server.get("/", homeController::homePage);
        server.get("/contacts", searchContactsController::searchPage);
        server.get("/contacts/new", contactsController::createPage);
        server.post("/contacts", contactsController::createContact);
        server.get("/contacts/{id}", contactsController::contactInfo);
        server.post("/contacts/{id}/delete", contactsController::deleteContact);
        server.get("/notifications", notificationsController::notificationPage);
        server.get("/notifications/new", notificationsController::createNotificationPage);
        server.post("/notifications", notificationsController::createNotification);
        server.get("/notifications/{id}", notificationsController::notificationDetailsPage);
        server.post("/notifications/{id}/delete", notificationsController::deleteNotification);
        server.error(404, ErrorController::notFoundPage);
        server.error(500, ErrorController::internalServerErrorPage);
        server.exception(RuntimeException.class, (e, context) -> {
            LOGGER.error("Internal Server Error", e);
            context.status(500);
        });
        server.exception(ResourceNotFoundException.class, (e, context) -> {
            LOGGER.warn("Resource Not Found", e);
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

package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.CreateContactController;
import org.gbl.controller.ErrorController;
import org.gbl.controller.HomeController;
import org.gbl.controller.NotificationsController;
import org.gbl.controller.SearchContactsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web {

    private static final Logger LOGGER = LoggerFactory.getLogger(Web.class);

    private final Javalin server;
    private final HomeController homeController;
    private final SearchContactsController searchContactsController;
    private final ContactDetailsController contactDetailsController;
    private final CreateContactController createContactController;
    private final NotificationsController notificationsController;

    public Web(
            HomeController homeController,
            SearchContactsController searchContactsController,
            ContactDetailsController contactDetailsController,
            CreateContactController createContactController,
            NotificationsController notificationsController
    ) {
        this.homeController = homeController;
        this.searchContactsController = searchContactsController;
        this.contactDetailsController = contactDetailsController;
        this.createContactController = createContactController;
        this.notificationsController = notificationsController;
        server = Javalin.create(Web::configureServer);
        initRoutes();
    }

    private void initRoutes() {
        server.get("/", homeController::homePage);
        server.get("/contacts", searchContactsController::searchPage);
        server.get("/contacts/new", createContactController::createPage);
        server.post("/contacts", createContactController::createContact);
        server.get("/contacts/{id}", contactDetailsController::contactInfo);
        server.post("/contacts/{id}/delete", contactDetailsController::deleteContact);
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

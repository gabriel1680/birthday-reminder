package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.controller.ContactInfoController;
import org.gbl.controller.SearchContactsController;

public class Web {

    private final Javalin server;
    private final SearchContactsController searchContactsController;
    private final ContactInfoController contactInfoController;

    public Web(
            SearchContactsController searchContactsController,
            ContactInfoController contactInfoController
    ) {
        this.searchContactsController = searchContactsController;
        this.contactInfoController = contactInfoController;
        server = Javalin.create(Web::configureServer);
        initRoutes();
    }

    private void initRoutes() {
        server.get("/", searchContactsController::searchPage);
        server.get("/{id}", contactInfoController::contactInfo);
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

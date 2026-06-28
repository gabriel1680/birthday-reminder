package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.view.ContactSearchPresenter;

public class Web {

    private final Javalin server;
    private final ContactsController controller;

    public Web(ContactsGateway contactsGateway) {
        controller = new ContactsController(contactsGateway, new ContactSearchPresenter());
        server = Javalin.create(Web::configureServer);
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
        server.get("/", controller::searchPage);
        return server;
    }
}

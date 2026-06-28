package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ContactsGateway;

public class Web {

    private final Javalin server;
    private final ContactsController controller;

    public Web(ContactsGateway contactsGateway) {
        controller = new ContactsController(contactsGateway);
        server = Javalin.create(Web::configureServer);
    }

    private static void configureServer(JavalinConfig config) {
        config.fileRenderer(new JavalinJte());
    }

    public Javalin getServer() {
        server.get("/", controller::searchPage);
        return server;
    }
}

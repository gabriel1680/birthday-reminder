package org.gbl;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.controller.SearchContactsController;
import org.gbl.view.ContactSearchPresenter;

import java.time.Clock;
import java.util.concurrent.Executors;

public class Web {

    private final Javalin server;
    private final SearchContactsController controller;

    public Web(ContactsGateway contactsGateway) {
        final var clock = Clock.systemUTC();
        final var presenter = new ContactSearchPresenter(clock);
        final var factory = Thread.ofVirtual().name("contacts-search-pool", 0).factory();
        final var executor = Executors.newThreadPerTaskExecutor(factory);
        controller = new SearchContactsController(contactsGateway, presenter, executor);
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

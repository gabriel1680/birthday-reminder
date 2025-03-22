package org.gbl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.gbl.contacts.ContactsModuleFactory;

public class API {

    private final Javalin server;
    private final ProcessBuilder process;
    private final ContactsController contactsController;

    private API() {
        this.server = Javalin.create();
        this.process = new ProcessBuilder();
        final var contactsModule = ContactsModuleFactory.createInMemory();
        this.contactsController = new ContactsController(contactsModule);
    }

    public static API create() {
        var api = new API();
        api.configure();
        return api;
    }

    private void configure() {
        server.exception(JsonProcessingException.class, API::handleJsonException);
        server.post("/contacts", contactsController::create);
    }

    private static void handleJsonException(JsonProcessingException e, Context context) {
        context.contentType(ContentType.APPLICATION_JSON);
        context.status(HttpStatus.BAD_REQUEST);
        context.result(e.getMessage());
    }

    public void start() {
        final var port = process.environment().getOrDefault("PORT", "8080");
        server.start(Integer.parseInt(port));
    }

    public Javalin getServer() {
        return server;
    }
}

package org.gbl;

import org.gbl.contacts.ContactsModuleFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;

public class APIRoutes {

    private ContactsAPI contactsAPI;

    public void init() {
        createAPIs();
        swaggerRoutes();
        configureRoutes();
    }

    private void createAPIs() {
        var contactsModule = ContactsModuleFactory.createInMemory();
        contactsAPI = new ContactsAPI(contactsModule);
    }

    private void configureRoutes() {
        get("status", this::ok);
        post("/contacts", contactsAPI::createContact);
        get("/contacts", contactsAPI::searchContacts);
        get("/contacts/:id", contactsAPI::getContract);
        delete("/contacts/:id", contactsAPI::deleteContact);
        put("/contacts/:id", contactsAPI::updateContact);
    }

    private void swaggerRoutes() {
        options("status", this::ok);
        options("contacts", this::ok);
        options("contacts/:id", this::ok);
    }

    private String ok(Request req, Response res) {
        return "OK";
    }
}

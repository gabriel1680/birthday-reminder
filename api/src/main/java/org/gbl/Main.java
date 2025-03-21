package org.gbl;

import org.gbl.contacts.ContactsModuleFactory;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class Main {
    public static void main(String[] args) {
        var contactsModule = ContactsModuleFactory.createInMemory();
        var contactsAPI = new ContactsSparkController(contactsModule);

        port(getPort());
        post("/contacts", contactsAPI::createContact);
        get("/contacts/:id", contactsAPI::getContract);
        delete("/contacts/:id", contactsAPI::deleteContact);
        put("/contacts/:id", contactsAPI::updateContact);
    }

    private static int getPort() {
        var processBuilder = new ProcessBuilder();
        var port = processBuilder.environment().getOrDefault("PORT", "8080");
        return Integer.parseInt(port);
    }
}
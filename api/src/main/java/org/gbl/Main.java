package org.gbl;

import org.gbl.contacts.ContactsModuleFactory;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        final var contactsModule = ContactsModuleFactory.createInMemory();
        final var contactsAPI = new ContactsSparkController(contactsModule);

        port(8080);
        post("/contacts", contactsAPI::createContact);
        get("/contacts/:id", contactsAPI::getContract);
        delete("/contacts/:id", contactsAPI::deleteContact);
        patch("/contacts/:id", contactsAPI::updateContact);
    }
}
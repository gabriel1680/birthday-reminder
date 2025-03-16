package org.gbl;

import org.gbl.contacts.ContactsModuleFactory;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        final var contactsModule = ContactsModuleFactory.createInMemory();
        final var contactsAPI = new ContractsSparkController(contactsModule);

        post("/contacts", contactsAPI::createContact);
        get("/contacts/:id", contactsAPI::getContract);
    }
}
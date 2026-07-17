package org.gbl;

import org.gbl.contacts.ContactsModuleFactory;
import org.gbl.controller.contacts.ContactsAPI;
import org.gbl.controller.contacts.ContactsAPIImpl;
import org.gbl.controller.notifications.NotificationsAPI;
import org.gbl.controller.notifications.NotificationsAPIImpl;
import org.gbl.notification.NotificationsModuleFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;

class Routes {

    private ContactsAPI contactsAPI;
    private NotificationsAPI notificationsAPI;

    public void init() {
        createAPIs();
        swaggerRoutes();
        configureRoutes();
    }

    private void createAPIs() {
        final var contactsModule = ContactsModuleFactory.createInMemory();
        contactsAPI = new ContactsAPIImpl(contactsModule);
        final var notificationsModule = NotificationsModuleFactory.createInMemory();
        notificationsAPI = new NotificationsAPIImpl(notificationsModule);
    }

    private void configureRoutes() {
        // Status
        get("status", this::ok);
        // Contacts
        get("/contacts/upcoming-birthdays", contactsAPI::upcomingBirthdays);
        post("/contacts", contactsAPI::createContact);
        get("/contacts", contactsAPI::searchContacts);
        get("/contacts/:id", contactsAPI::getContact);
        delete("/contacts/:id", contactsAPI::deleteContact);
        put("/contacts/:id", contactsAPI::updateContact);
        // Notifications
        post("/notifications", notificationsAPI::createNotification);
        get("/notifications", notificationsAPI::getAllNotifications);
        get("/notifications/:id", notificationsAPI::getNotification);
        delete("/notifications/:id", notificationsAPI::deleteNotification);
    }

    private void swaggerRoutes() {
        options("status", this::ok);
        options("contacts", this::ok);
        options("contacts/:id", this::ok);
        options("notifications", this::ok);
        options("notifications/:id", this::ok);
    }

    private String ok(Request req, Response res) {
        return "OK";
    }
}

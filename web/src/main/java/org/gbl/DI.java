package org.gbl;

import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.http.HttpContactGateway;
import org.gbl.common.service.json.GsonJsonParser;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.SearchContactsController;
import org.gbl.view.ContactSearchPresenter;

import java.net.http.HttpClient;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DI {

    public static Web createWebApp(ContactsGateway gateway) {
        final var searchContactsController = searchContactsController(gateway);
        final var contactInfoController = contactInfoController(gateway);
        return new Web(searchContactsController, contactInfoController);
    }

    private static ContactDetailsController contactInfoController(ContactsGateway gateway) {
        final var contactInfoController = new ContactDetailsController(gateway);
        return contactInfoController;
    }

    private static SearchContactsController searchContactsController(ContactsGateway gateway) {
        final var executor = executorService("contacts-search-pool");
        final var clock = Clock.systemUTC();
        final var presenter = new ContactSearchPresenter(clock);
        return new SearchContactsController(gateway, presenter, executor);
    }

    private static ExecutorService executorService(String contactsSearchPool) {
        final var factory = Thread.ofVirtual().name(contactsSearchPool, 0).factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }

    public static HttpContactGateway httpContactGateway() {
        final var httpClient = HttpClient.newHttpClient();
        final var jsonParser = new GsonJsonParser();
        final var url = "http://localhost:8080";
        return new HttpContactGateway(jsonParser, httpClient, url);
    }
}

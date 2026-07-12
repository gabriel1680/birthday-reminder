package org.gbl;

import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.http.HttpContactGateway;
import org.gbl.common.gateway.http.HttpApiClient;
import org.gbl.common.notification.HttpNotificationGateway;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.service.json.GsonJsonParser;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.NotificationsController;
import org.gbl.controller.SearchContactsController;
import org.gbl.view.ContactSearchPresenter;

import java.net.http.HttpClient;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DI {

    public static Web createWebApp(ContactsGateway gateway, NotificationGateway notificationGateway) {
        final var searchContactsController = searchContactsController(gateway);
        final var contactInfoController = contactInfoController(gateway);
        final var notificationsController = notificationsController(notificationGateway);
        return new Web(searchContactsController, contactInfoController, notificationsController);
    }

    private static NotificationsController notificationsController(NotificationGateway notificationGateway) {
        final var notificationsController = new NotificationsController(notificationGateway);
        return notificationsController;
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

    private static ExecutorService executorService(String poolName) {
        final var factory = Thread.ofVirtual().name(poolName, 0).factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }

    public static HttpContactGateway httpContactGateway() {
        final var httpRestClient = httpApiClient();
        return new HttpContactGateway(httpRestClient);
    }

    public static NotificationGateway notificationGateway() {
        final var httpRestClient = httpApiClient();
        return new HttpNotificationGateway(httpRestClient);
    }

    private static HttpApiClient httpApiClient() {
        final var httpClient = HttpClient.newHttpClient();
        final var jsonParser = new GsonJsonParser();
        final var url = "http://localhost:8080";
        return new HttpApiClient(jsonParser, httpClient, url);
    }
}

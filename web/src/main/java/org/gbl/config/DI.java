package org.gbl.config;

import org.gbl.app.Web;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.http.HttpApiClient;
import org.gbl.common.gateway.http.HttpContactGateway;
import org.gbl.common.notification.HttpNotificationGateway;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.service.json.GsonJsonServiceAdapter;
import org.gbl.controller.ContactsController;
import org.gbl.controller.HomeController;
import org.gbl.controller.NotificationsController;
import org.gbl.controller.SearchContactsController;
import org.gbl.presenter.ContactSearchPresenter;
import org.gbl.presenter.UpcomingBirthdaysPresenter;
import org.gbl.service.ContactsService;
import org.gbl.service.NotificationService;

import java.net.http.HttpClient;
import java.time.Clock;

public class DI {

    public static Web createWebApp(ContactsGateway gateway, NotificationGateway notificationGateway) {
        final var homeController = homeController(gateway);
        final var searchContactsController = searchContactsController(gateway);
        final var contactsService = contactsService(gateway);
        final var contactsController = contactsController(contactsService);
        final var notificationService = notificationService(notificationGateway);
        final var notificationsController = notificationsController(notificationService);
        return new Web(homeController, searchContactsController, contactsController, notificationsController);
    }

    private static NotificationService notificationService(NotificationGateway gateway) {
        return new NotificationService(gateway);
    }

    private static HomeController homeController(ContactsGateway gateway) {
        return new HomeController(gateway, upcomingBirthdaysPresenter());
    }

    private static NotificationsController notificationsController(NotificationService notificationService) {
        return new NotificationsController(notificationService);
    }

    private static ContactsController contactsController(ContactsService service) {
        return new ContactsController(service);
    }

    private static ContactsService contactsService(ContactsGateway gateway) {
        return new ContactsService(gateway);
    }

    private static SearchContactsController searchContactsController(ContactsGateway gateway) {
        return new SearchContactsController(gateway, new ContactSearchPresenter());
    }

    private static UpcomingBirthdaysPresenter upcomingBirthdaysPresenter() {
        return new UpcomingBirthdaysPresenter(Clock.systemUTC());
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
        final var jsonParser = new GsonJsonServiceAdapter();
        final var url = "http://localhost:8080";
        return new HttpApiClient(jsonParser, httpClient, url);
    }
}

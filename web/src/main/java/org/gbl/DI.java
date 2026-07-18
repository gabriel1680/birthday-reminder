package org.gbl;

import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.http.HttpApiClient;
import org.gbl.common.gateway.http.HttpContactGateway;
import org.gbl.common.notification.HttpNotificationGateway;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.service.json.GsonJsonServiceAdapter;
import org.gbl.controller.ContactDetailsController;
import org.gbl.controller.HomeController;
import org.gbl.controller.NotificationsController;
import org.gbl.controller.SearchContactsController;
import org.gbl.view.contacts.ContactSearchPresenter;
import org.gbl.view.contacts.UpcomingBirthdaysPresenter;

import java.net.http.HttpClient;
import java.time.Clock;

public class DI {

    public static Web createWebApp(ContactsGateway gateway, NotificationGateway notificationGateway) {
        final var homeController = homeController(gateway);
        final var searchContactsController = searchContactsController(gateway);
        final var contactInfoController = contactInfoController(gateway);
        final var notificationsController = notificationsController(notificationGateway);
        return new Web(homeController, searchContactsController, contactInfoController, notificationsController);
    }

    private static HomeController homeController(ContactsGateway gateway) {
        return new HomeController(gateway, upcomingBirthdaysPresenter());
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

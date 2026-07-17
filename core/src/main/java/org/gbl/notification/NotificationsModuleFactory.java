package org.gbl.notification;

import org.gbl.notification.application.NotificationService;
import org.gbl.reminder.out.notification.InMemoryNotificationRepository;

public class NotificationsModuleFactory {

    public static NotificationsModule createInMemory() {
        final var repository = new InMemoryNotificationRepository();
        final var service = new NotificationService(repository);
        return new NotificationsModuleImpl(service);
    }
}

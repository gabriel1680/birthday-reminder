package org.gbl.notification;

import org.gbl.notification.application.NotificationService;
import org.gbl.reminder.out.notification.InMemoryNotificationRepository;

public class NotificationModuleFactory {
    public static NotificationModule create() {
        final var repository = new InMemoryNotificationRepository();
        final var service = new NotificationService(repository);
        return new NotificationModuleImpl(service);
    }
}

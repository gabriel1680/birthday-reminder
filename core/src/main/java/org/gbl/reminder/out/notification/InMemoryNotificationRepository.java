package org.gbl.reminder.out.notification;

import org.gbl.reminder.app.domain.NotificationMethod;
import org.gbl.reminder.app.domain.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotificationRepository implements NotificationRepository {

    private final List<NotificationMethod> notificationMethods = new ArrayList<>();

    @Override
    public void add(NotificationMethod aMethod) {
        notificationMethods.add(aMethod);
    }

    @Override
    public List<NotificationMethod> all() {
        return notificationMethods;
    }
}

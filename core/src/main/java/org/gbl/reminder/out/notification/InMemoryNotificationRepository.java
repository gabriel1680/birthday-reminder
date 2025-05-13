package org.gbl.reminder.out.notification;

import org.gbl.notification.domain.Notification;
import org.gbl.notification.domain.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryNotificationRepository implements NotificationRepository {

    private final List<Notification> notifications = new ArrayList<>();

    @Override
    public void add(Notification notification) {
        notifications.add(notification);
    }

    @Override
    public Optional<Notification> getById(String anId) {
        return notifications.stream().filter(n -> n.id().equals(anId)).findFirst();
    }

    @Override
    public void remove(Notification aNotification) {
        notifications.remove(aNotification);
    }

    @Override
    public List<Notification> all() {
        return notifications;
    }
}

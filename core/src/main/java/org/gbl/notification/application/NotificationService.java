package org.gbl.notification.application;

import org.gbl.notification.NotificationsModule;
import org.gbl.notification.domain.Notification;
import org.gbl.notification.domain.NotificationRepository;

import java.util.List;

public class NotificationService implements NotificationsModule {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void add(AddNotificationInput input) {
        final var aNotification = Notification.create(input.type(), input.value());
        repository.add(aNotification);
    }

    @Override
    public void removeOf(String id) {
        repository.remove(getNotification(id));
    }

    @Override
    public List<NotificationOutput> all() {
        return repository.all().stream()
                .map(NotificationService::toOutput)
                .toList();
    }

    @Override
    public NotificationOutput getOf(String id) {
        return toOutput(getNotification(id));
    }

    private Notification getNotification(String request) {
        return repository.getById(request)
                .orElseThrow(NotificationNotFoundException::new);
    }

    private static NotificationOutput toOutput(Notification n) {
        return new NotificationOutput(n.id(), n.type().value(), n.value());
    }
}

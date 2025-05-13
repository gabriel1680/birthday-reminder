package org.gbl.notification.application;

import org.gbl.notification.domain.Notification;
import org.gbl.notification.domain.NotificationRepository;

import java.util.List;

public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void add(AddNotificationRequest request) {
        final var aNotification = Notification.create(request.type(), request.value());
        repository.add(aNotification);
    }

    public void update(UpdateNotificationRequest request) {
        final var notification = getNotification(request.id());
        notification.update(request.value());
    }

    public void remove(String id) {
        repository.remove(getNotification(id));
    }

    private Notification getNotification(String request) {
        return repository.getById(request)
                .orElseThrow(NotificationNotFoundException::new);
    }

    public List<NotificationResponse> all() {
        return repository.all().stream()
                .map(n -> new NotificationResponse(n.id(), n.type().value(), n.value()))
                .toList();
    }
}

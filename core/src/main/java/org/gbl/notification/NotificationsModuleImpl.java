package org.gbl.notification;

import org.gbl.notification.application.AddNotificationInput;
import org.gbl.notification.application.NotificationOutput;
import org.gbl.notification.application.NotificationService;

import java.util.List;

public class NotificationsModuleImpl implements NotificationsModule {

    private final NotificationService service;

    public NotificationsModuleImpl(NotificationService service) {
        this.service = service;
    }

    @Override
    public void add(AddNotificationInput request) {
        service.add(request);
    }

    @Override
    public void removeOf(String id) {
        service.removeOf(id);
    }

    @Override
    public List<NotificationOutput> all() {
        return service.all();
    }

    @Override
    public NotificationOutput getOf(String id) {
        return service.getOf(id);
    }
}

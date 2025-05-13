package org.gbl.notification;

import org.gbl.notification.application.AddNotificationRequest;
import org.gbl.notification.application.NotificationResponse;
import org.gbl.notification.application.NotificationService;
import org.gbl.notification.application.UpdateNotificationRequest;

import java.util.List;

public class NotificationModuleImpl implements NotificationModule {

    private final NotificationService service;

    public NotificationModuleImpl(NotificationService service) {
        this.service = service;
    }

    @Override
    public void add(AddNotificationRequest request) {
        service.add(request);
    }

    @Override
    public void update(UpdateNotificationRequest request) {
        service.update(request);
    }

    @Override
    public void remove(String id) {
        service.remove(id);
    }

    @Override
    public List<NotificationResponse> all() {
        return service.all();
    }
}

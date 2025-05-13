package org.gbl.reminder.fixture;

import org.gbl.notification.NotificationModule;
import org.gbl.notification.application.AddNotificationRequest;
import org.gbl.notification.application.NotificationResponse;
import org.gbl.notification.application.UpdateNotificationRequest;

import java.util.List;

public class FakeNotificationModule implements NotificationModule {

    @Override
    public void add(AddNotificationRequest request) {
    }

    @Override
    public void update(UpdateNotificationRequest request) {
    }

    @Override
    public void remove(String id) {
    }

    @Override
    public List<NotificationResponse> all() {
        return List.of();
    }
}

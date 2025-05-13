package org.gbl.notification;

import org.gbl.notification.application.AddNotificationRequest;
import org.gbl.notification.application.NotificationResponse;
import org.gbl.notification.application.UpdateNotificationRequest;

import java.util.List;

public interface NotificationModule {
    void add(AddNotificationRequest request);

    void update(UpdateNotificationRequest request);

    void remove(String id);

    List<NotificationResponse> all();
}

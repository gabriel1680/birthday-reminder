package org.gbl.notification;

import org.gbl.notification.application.AddNotificationInput;
import org.gbl.notification.application.NotificationOutput;

import java.util.List;

public interface NotificationsModule {
    void add(AddNotificationInput request);

    void removeOf(String id);

    List<NotificationOutput> all();

    NotificationOutput getOf(String id);
}

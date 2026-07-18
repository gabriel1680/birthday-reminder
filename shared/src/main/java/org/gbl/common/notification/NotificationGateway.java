package org.gbl.common.notification;

import java.util.List;

public interface NotificationGateway {
    List<NotificationResponse> getAll();

    NotificationResponse get(String id);

    void add(AddNotificationRequest request);

    void remove(RemoveNotificationRequest request);
}

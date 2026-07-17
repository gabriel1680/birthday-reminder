package org.gbl.common.notification;

import io.vavr.control.Try;

import java.util.List;

public interface NotificationGateway {
    Try<List<NotificationResponse>> getAll();

    Try<NotificationResponse> get(String id);

    Try<Void> add(AddNotificationRequest request);

    Try<Void> remove(RemoveNotificationRequest request);
}

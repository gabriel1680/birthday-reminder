package org.gbl.notification.domain;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    List<Notification> all();

    void add(Notification aMethod);

    Optional<Notification> getById(String anId);

    void remove(Notification aNotification);
}

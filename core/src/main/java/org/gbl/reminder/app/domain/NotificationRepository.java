package org.gbl.reminder.app.domain;

import java.util.List;

public interface NotificationRepository {
    List<NotificationMethod> all();

    void add(NotificationMethod aMethod);
}

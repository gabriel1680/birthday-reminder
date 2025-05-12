package org.gbl.reminder.app;

import java.util.List;

public interface NotificationRepository {
    List<NotificationMethod> all();

    void add(NotificationMethod aMethod);
}

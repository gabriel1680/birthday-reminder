package org.gbl.reminder.app;

import java.util.Objects;

public class NotificationMethod {
    private final NotificationType notificationType;
    private final String notificationValue;

    public NotificationMethod(NotificationType notificationType, String notificationValue) {
        this.notificationType = notificationType;
        this.notificationValue = notificationValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationMethod that = (NotificationMethod) o;
        return notificationType == that.notificationType && Objects.equals(notificationValue, that.notificationValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationType, notificationValue);
    }
}

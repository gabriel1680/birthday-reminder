package org.gbl.reminder.app.domain;

import java.util.Objects;

public class NotificationMethod {
    private final NotificationType type;
    private final String value;

    protected NotificationMethod(NotificationType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static NotificationMethod create(String notificationType, String notificationValue) {
        var type = NotificationType.of(notificationType);
        return switch (type) {
            case EMAIL -> new EmailNotification(notificationValue);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationMethod that = (NotificationMethod) o;
        return type == that.type && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}

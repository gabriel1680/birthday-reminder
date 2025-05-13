package org.gbl.notification.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class Notification {
    private final String id;
    private final NotificationType type;
    private String value;

    protected Notification(NotificationType type, String value) {
        throwIfInvalid(value);
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.value = value;
    }

    public static Notification create(String notificationType, String notificationValue) {
        var type = NotificationType.of(notificationType);
        return switch (type) {
            case EMAIL -> new EmailNotification(notificationValue);
        };
    }

    public void update(String newValue) {
        throwIfInvalid(newValue);
        value = newValue;
    }

    protected abstract void throwIfInvalid(String aValue);

    public NotificationType type() {
        return type;
    }

    public String value() {
        return value;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

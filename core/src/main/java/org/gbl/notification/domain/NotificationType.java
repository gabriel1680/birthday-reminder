package org.gbl.notification.domain;

public enum NotificationType {
    EMAIL("email");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public static NotificationType of(String s) {
        for (var type : values()) {
            if (type.value.equals(s)) return type;
        }
        throw new IllegalArgumentException("invalid notification type");
    }

    public String value() {
        return value;
    }
}

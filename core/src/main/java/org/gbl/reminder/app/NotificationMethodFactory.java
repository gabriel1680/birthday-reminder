package org.gbl.reminder.app;

public class NotificationMethodFactory {
    public NotificationMethod create(String notificationType, String notificationValue) {
        var type = NotificationType.of(notificationType);
        return switch (type) {
            case EMAIL -> new EmailNotificationMethod(notificationValue);
        };
    }
}

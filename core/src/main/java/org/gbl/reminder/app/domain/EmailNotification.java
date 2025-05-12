package org.gbl.reminder.app.domain;

import java.util.regex.Pattern;

class EmailNotification extends NotificationMethod {

    private static final String emailPattern = "^(.+)@(\\S+)$";

    public EmailNotification(String notificationValue) {
        super(NotificationType.EMAIL, notificationValue);
        if (!isValidEmail(notificationValue))
            throw new IllegalArgumentException("invalid email");
    }

    private static boolean isValidEmail(String notificationValue) {
        return Pattern.compile(emailPattern)
                .matcher(notificationValue)
                .matches();
    }
}

package org.gbl.reminder.app;

import java.util.regex.Pattern;

public class EmailNotificationMethod extends NotificationMethod {

    private static final String emailPattern = "^(.+)@(\\S+)$";

    public EmailNotificationMethod(String notificationValue) {
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

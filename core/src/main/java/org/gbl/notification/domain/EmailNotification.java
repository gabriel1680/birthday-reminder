package org.gbl.notification.domain;

import java.util.regex.Pattern;

class EmailNotification extends Notification {

    private static final String emailPattern = "^(.+)@(\\S+)$";

    public EmailNotification(String value) {
        super(NotificationType.EMAIL, value);
    }

    private static boolean isValidEmail(String notificationValue) {
        return Pattern.compile(emailPattern)
                .matcher(notificationValue)
                .matches();
    }

    @Override
    protected void throwIfInvalid(String aValue) {
        if (!isValidEmail(aValue))
            throw new IllegalArgumentException("invalid email");
    }
}

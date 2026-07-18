package org.gbl.view.notification;

public record CreateNotificationViewModel(String value, String valueError) {

    public static CreateNotificationViewModel empty() {
        return new CreateNotificationViewModel("", null);
    }
}

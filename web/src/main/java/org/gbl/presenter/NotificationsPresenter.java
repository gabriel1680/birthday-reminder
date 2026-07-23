package org.gbl.presenter;

import org.gbl.common.notification.NotificationResponse;
import org.gbl.view.notification.CreateNotificationViewModel;
import org.gbl.view.notification.NotificationViewModel;
import org.gbl.view.notification.NotificationsViewModel;

import java.util.List;

public class NotificationsPresenter {

    public NotificationsViewModel toNotificationsList(List<NotificationResponse> notificationResponses) {
        final var notifications = notificationResponses.stream()
                .map(this::toNotification)
                .toList();
        return new NotificationsViewModel(notifications.size(), notifications);
    }

    public NotificationViewModel toNotification(NotificationResponse it) {
        return new NotificationViewModel(it.id(), it.type(), it.value());
    }

    public CreateNotificationViewModel toNotificationError(String value, String valueError) {
        return new CreateNotificationViewModel(value, valueError);
    }

    public CreateNotificationViewModel toNotificationEmpty() {
        return CreateNotificationViewModel.empty();
    }
}

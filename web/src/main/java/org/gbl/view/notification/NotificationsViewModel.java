package org.gbl.view.notification;

import java.util.Collection;

public record NotificationsViewModel(int total, Collection<NotificationViewModel> notifications) {
}

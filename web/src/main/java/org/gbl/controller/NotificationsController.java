package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.view.notification.NotificationViewModel;
import org.gbl.view.notification.NotificationsViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class NotificationsController {
    private final NotificationGateway notificationGateway;

    public NotificationsController(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void notificationPage(Context context) {
        final var notifications = notificationGateway.getAll().get();
        final var viewModel = Map.of("viewModel", createViewModelFrom(notifications));
        context.render("notifications/notifications-page.jte", viewModel);
    }

    private static NotificationsViewModel createViewModelFrom(List<NotificationResponse> notificationResponses) {
        final var notifications = notificationResponses.stream()
                .map(NotificationsController::notificationViewModel)
                .toList();
        return new NotificationsViewModel(notifications.size(), notifications);
    }

    private static NotificationViewModel notificationViewModel(NotificationResponse it) {
        return new NotificationViewModel(it.id(), it.type(), it.value());
    }
}

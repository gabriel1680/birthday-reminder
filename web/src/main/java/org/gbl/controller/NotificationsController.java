package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.common.notification.RemoveNotificationRequest;
import org.gbl.view.notification.NotificationViewModel;
import org.gbl.view.notification.NotificationsViewModel;

import java.util.List;
import java.util.Map;

public class NotificationsController {
    private final NotificationGateway notificationGateway;

    public NotificationsController(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void notificationPage(Context context) {
        final var notifications = notificationGateway.getAll();
        final var viewModel = Map.of("viewModel", createViewModelFrom(notifications));
        context.render("notifications/notifications-page.jte", viewModel);
    }

    public void notificationDetailsPage(Context context) {
        final var notification = notificationGateway.get(context.pathParam("id"));
        final var viewModel = notificationViewModel(notification);
        context.render("notifications/details-page.jte", Map.of("viewModel", viewModel));
    }

    public void deleteNotification(Context context) {
        notificationGateway.remove(new RemoveNotificationRequest(context.pathParam("id")));
        context.redirect("/notifications");
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

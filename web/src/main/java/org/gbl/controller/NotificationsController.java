package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.notification.AddNotificationRequest;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.common.notification.RemoveNotificationRequest;
import org.gbl.view.notification.CreateNotificationViewModel;
import org.gbl.view.notification.NotificationViewModel;
import org.gbl.view.notification.NotificationsViewModel;

import java.util.List;
import java.util.Map;

public class NotificationsController {
    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private final NotificationGateway notificationGateway;

    public NotificationsController(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void notificationPage(Context context) {
        final var notifications = notificationGateway.getAll();
        final var viewModel = Map.of("viewModel", createViewModelFrom(notifications));
        context.render("notifications/notifications-page.jte", viewModel);
    }

    public void createNotificationPage(Context context) {
        renderCreatePage(context, CreateNotificationViewModel.empty());
    }

    public void createNotification(Context context) {
        final var value = valueOrEmpty(context.formParam("value")).trim();
        final String valueError;
        if (value.isBlank()) {
            valueError = "Enter an email address.";
        } else if (!value.matches(EMAIL_PATTERN)) {
            valueError = "Enter a valid email address.";
        } else {
            valueError = null;
        }

        if (valueError != null) {
            context.status(400);
            renderCreatePage(context, new CreateNotificationViewModel(value, valueError));
            return;
        }

        notificationGateway.add(new AddNotificationRequest("email", value));
        context.redirect("/notifications");
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

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private static void renderCreatePage(Context context, CreateNotificationViewModel viewModel) {
        context.render("notifications/create-page.jte", Map.of("viewModel", viewModel));
    }
}

package org.gbl.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.gbl.form.CreateNotificationForm;
import org.gbl.presenter.NotificationsPresenter;
import org.gbl.service.NotificationService;
import org.gbl.validation.InvalidNotificationFormException;

import static org.gbl.config.JTEPages.NOTIFICATION_CREATE_PAGE;
import static org.gbl.config.JTEPages.NOTIFICATION_DETAILS_PAGE;
import static org.gbl.config.JTEPages.NOTIFICATION_LIST_PAGE;
import static org.gbl.config.Routes.notifications;

public class NotificationsController extends JavalinController {

    private final NotificationService service;
    private final NotificationsPresenter presenter;

    public NotificationsController(NotificationService notificationService) {
        this.service = notificationService;
        presenter = new NotificationsPresenter();
    }

    public void notificationPage(Context context) {
        final var notifications = service.getAll();
        final var viewModel = presenter.toNotificationsList((notifications));
        context.render(NOTIFICATION_LIST_PAGE, toViewModelMap(viewModel));
    }

    public void createNotificationPage(Context context) {
        context.render(NOTIFICATION_CREATE_PAGE, toViewModelMap(presenter.toNotificationEmpty()));
    }

    public void createNotification(Context context) {
        final var value = context.formParam("value");
        final var type = context.formParam("type");
        final var form = new CreateNotificationForm(type, value);
        service.createNotification(form)
                .peekLeft(exception -> handleFormError(context, exception, value))
                .peek(v -> context.redirect(notifications()));
    }

    private void handleFormError(Context context, InvalidNotificationFormException exception,
                                 String value) {
        context.status(HttpStatus.BAD_REQUEST);
        final var viewModel = presenter.toNotificationError(
                value,
                exception.validation().valueError());
        context.render(NOTIFICATION_CREATE_PAGE, toViewModelMap(viewModel));
    }

    public void notificationDetailsPage(Context context) {
        final var notification = service.getOf(idFrom(context));
        final var viewModel = presenter.toNotification(notification);
        context.render(NOTIFICATION_DETAILS_PAGE, toViewModelMap(viewModel));
    }

    public void deleteNotification(Context context) {
        service.deleteOf(idFrom(context));
        context.redirect(notifications());
    }
}

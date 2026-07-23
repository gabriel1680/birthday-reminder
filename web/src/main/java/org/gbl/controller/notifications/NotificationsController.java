package org.gbl.controller.notifications;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.gbl.controller.JavalinController;

public class NotificationsController extends JavalinController {

    private final static String DETAILS_PAGE = "notifications/details-page.jte";
    private final static String CREATE_PAGE = "notifications/create-page.jte";
    private final static String LIST_PAGE = "notifications/notifications-page.jte";

    private final NotificationService service;
    private final NotificationsPresenter presenter;

    public NotificationsController(NotificationService notificationService) {
        this.service = notificationService;
        presenter = new NotificationsPresenter();
    }

    public void notificationPage(Context context) {
        final var notifications = service.getAll();
        final var viewModel = presenter.toNotificationsList((notifications));
        context.render(LIST_PAGE, toViewModelMap(viewModel));
    }

    public void createNotificationPage(Context context) {
        context.render(CREATE_PAGE, toViewModelMap(presenter.toNotificationEmpty()));
    }

    public void createNotification(Context context) {
        final var value = context.formParam("value");
        final var type = context.formParam("type");
        final var form = new CreateNotificationForm(type, value);
        service.createNotification(form)
                .peekLeft(exception -> handleFormError(context, exception, value))
                .peek(v -> context.redirect("/notifications"));
    }

    private void handleFormError(Context context, InvalidNotificationFormException exception,
                                 String value) {
        context.status(HttpStatus.BAD_REQUEST);
        final var viewModel = presenter.toNotificationError(
                value,
                exception.validation().valueError());
        context.render(CREATE_PAGE, toViewModelMap(viewModel));
    }

    public void notificationDetailsPage(Context context) {
        final var notification = service.getOf(idFrom(context));
        final var viewModel = presenter.toNotification(notification);
        context.render(DETAILS_PAGE, toViewModelMap(viewModel));
    }

    public void deleteNotification(Context context) {
        service.deleteOf(idFrom(context));
        context.redirect("/notifications");
    }
}

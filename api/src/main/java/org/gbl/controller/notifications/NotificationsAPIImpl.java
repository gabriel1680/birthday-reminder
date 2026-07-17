package org.gbl.controller.notifications;

import org.eclipse.jetty.http.HttpStatus;
import org.gbl.controller.common.HttpAPIResponse;
import org.gbl.notification.NotificationsModule;
import spark.Request;
import spark.Response;

public class NotificationsAPIImpl implements NotificationsAPI {

    private final NotificationsModule notificationsModule;
    private final NotificationsRequestParser requestParser;
    private final NotificationsJSONPresenter presenter;

    public NotificationsAPIImpl(NotificationsModule notificationsModule) {
        this.notificationsModule = notificationsModule;
        this.requestParser = new NotificationsRequestParser();
        this.presenter = new NotificationsJSONPresenter();
    }

    @Override
    public HttpAPIResponse createNotification(Request request, Response response) {
        final var input = requestParser.parseBody(request);
        notificationsModule.add(input);
        response.status(HttpStatus.CREATED_201);
        return HttpAPIResponse.ofSuccess();
    }

    @Override
    public HttpAPIResponse getAllNotifications(Request request, Response response) {
        final var notifications = notificationsModule.all();
        response.status(HttpStatus.OK_200);
        return HttpAPIResponse.ofSuccess(presenter.toJson(notifications));
    }

    @Override
    public HttpAPIResponse getNotification(Request request, Response response) {
        final var id = requestParser.getId(request);
        final var notification = notificationsModule.getOf(id);
        response.status(HttpStatus.OK_200);
        return HttpAPIResponse.ofSuccess(presenter.toJson(notification));
    }

    @Override
    public HttpAPIResponse deleteNotification(Request request, Response response) {
        final var id = requestParser.getId(request);
        notificationsModule.removeOf(id);
        response.status(HttpStatus.NO_CONTENT_204);
        return HttpAPIResponse.ofSuccess();
    }
}

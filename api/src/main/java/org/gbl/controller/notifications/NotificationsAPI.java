package org.gbl.controller.notifications;

import org.gbl.controller.common.HttpAPIResponse;
import spark.Request;
import spark.Response;

public interface NotificationsAPI {

    HttpAPIResponse createNotification(Request request, Response response);

    HttpAPIResponse getAllNotifications(Request request, Response response);

    HttpAPIResponse getNotification(Request request, Response response);

    HttpAPIResponse deleteNotification(Request request, Response response);
}

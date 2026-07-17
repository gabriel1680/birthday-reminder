package org.gbl.controller.notifications;

import org.gbl.controller.common.RequestParser;
import org.gbl.controller.common.InvalidPayloadException;
import org.gbl.notification.application.AddNotificationInput;
import org.json.JSONObject;
import spark.Request;

public class NotificationsRequestParser extends RequestParser {

    public AddNotificationInput parseBody(Request request) {
        try {
            final var json = new JSONObject(request.body());
            final var type = json.getString("type");
            final var value = json.getString("value");
            return new AddNotificationInput(type, value);
        } catch (RuntimeException e) {
            throw new InvalidPayloadException(e);
        }
    }
}

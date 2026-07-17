package org.gbl.controller.notifications;

import org.gbl.notification.application.NotificationOutput;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

public class NotificationsJSONPresenter {

    public JSONObject toJson(NotificationOutput output) {
        return new JSONObject()
                .put("id", output.id())
                .put("type", output.type())
                .put("value", output.value());
    }

    public JSONArray toJson(Collection<NotificationOutput> output) {
        return output.stream()
                .reduce(new JSONArray(),
                        (acc, next) -> acc.put(toJson(next)),
                        JSONArray::putAll);
    }
}

package org.gbl.common.notification;

import com.google.gson.reflect.TypeToken;
import io.vavr.control.Try;
import org.gbl.common.gateway.http.HttpApiClient;

import java.lang.reflect.Type;
import java.util.List;

public class HttpNotificationGateway implements  NotificationGateway {

    private static final String RESOURCE = "/notifications";

    private static final Type NOTIFICATIONS_RESPONSE_TYPE =
            TypeToken.getParameterized(List.class, NotificationResponse.class)
                    .getType();
    private static final Type NOTIFICATION_RESPONSE_TYPE = NotificationResponse.class;

    private final HttpApiClient httpApiClient;

    public HttpNotificationGateway(HttpApiClient httpApiClient) {
        this.httpApiClient = httpApiClient;
    }

    @Override
    public Try<List<NotificationResponse>> getAll() {
        return httpApiClient.get(RESOURCE, NOTIFICATIONS_RESPONSE_TYPE);
    }

    @Override
    public Try<NotificationResponse> get(String id) {
        return httpApiClient.get(RESOURCE + "/" + id, NOTIFICATION_RESPONSE_TYPE);
    }

    @Override
    public Try<Void> add(AddNotificationRequest request) {
        return httpApiClient.post(RESOURCE, request, NOTIFICATION_RESPONSE_TYPE);
    }

    @Override
    public Try<Void> remove(RemoveNotificationRequest request) {
        return httpApiClient.delete(RESOURCE + "/" + request.id(), NOTIFICATION_RESPONSE_TYPE);
    }
}

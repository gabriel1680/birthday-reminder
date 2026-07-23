package org.gbl.controller.notifications;

import io.vavr.control.Either;
import org.gbl.common.notification.AddNotificationRequest;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.common.notification.RemoveNotificationRequest;

import java.util.List;

public class NotificationService {

    private final NotificationGateway notificationGateway;
    private final CreateNotificationValidator createNotificationValidator;

    public NotificationService(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
        createNotificationValidator = new CreateNotificationValidator();
    }

    public Either<InvalidNotificationFormException, Void> createNotification(CreateNotificationForm form) {
        final var validation = createNotificationValidator.validate(form);
        if (validation.hasErrors()) {
            return Either.left(new InvalidNotificationFormException(validation));
        }
        final var request = new AddNotificationRequest(validation.type(), validation.value());
        notificationGateway.add(request);
        return Either.right(null);
    }

    public NotificationResponse getOf(String id) {
        return notificationGateway.get(id);
    }

    public void deleteOf(String id) {
        notificationGateway.remove(new RemoveNotificationRequest(id));
    }

    public List<NotificationResponse> getAll() {
        return notificationGateway.getAll();
    }
}

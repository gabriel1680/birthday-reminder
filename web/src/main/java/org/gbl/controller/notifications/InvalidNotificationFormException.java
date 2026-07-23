package org.gbl.controller.notifications;

public class InvalidNotificationFormException extends RuntimeException {

    private final CreateNotificationValidation validation;

    public InvalidNotificationFormException(CreateNotificationValidation validation) {
        this.validation = validation;
    }

    public CreateNotificationValidation validation() {
        return validation;
    }
}

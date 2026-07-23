package org.gbl.exception;

import org.gbl.validation.CreateNotificationValidation;

public class InvalidNotificationFormException extends RuntimeException {

    private final CreateNotificationValidation validation;

    public InvalidNotificationFormException(CreateNotificationValidation validation) {
        this.validation = validation;
    }

    public CreateNotificationValidation validation() {
        return validation;
    }
}

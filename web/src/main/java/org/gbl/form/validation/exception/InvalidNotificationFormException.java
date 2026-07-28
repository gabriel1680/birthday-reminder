package org.gbl.form.validation.exception;

import org.gbl.form.validation.CreateNotificationValidation;

public class InvalidNotificationFormException extends RuntimeException {

    private final CreateNotificationValidation validation;

    public InvalidNotificationFormException(CreateNotificationValidation validation) {
        this.validation = validation;
    }

    public CreateNotificationValidation validation() {
        return validation;
    }
}

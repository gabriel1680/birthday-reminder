package org.gbl.exception;

import org.gbl.validation.CreateContactValidation;

public class InvalidContactFormException extends RuntimeException {

    private final CreateContactValidation validation;

    public InvalidContactFormException(CreateContactValidation validation) {
        this.validation = validation;
    }

    public CreateContactValidation validation() {
        return validation;
    }
}

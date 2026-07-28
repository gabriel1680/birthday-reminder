package org.gbl.form.validation.exception;

import org.gbl.form.validation.CreateContactValidation;

public class InvalidContactFormException extends RuntimeException {

    private final CreateContactValidation validation;

    public InvalidContactFormException(CreateContactValidation validation) {
        this.validation = validation;
    }

    public CreateContactValidation validation() {
        return validation;
    }
}

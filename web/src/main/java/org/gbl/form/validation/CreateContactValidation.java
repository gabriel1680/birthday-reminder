package org.gbl.form.validation;

import java.time.LocalDate;

public record CreateContactValidation(
        String name,
        LocalDate birthdate,
        String nameError,
        String birthdateError
) {
    public boolean hasErrors() {
        return nameError != null || birthdateError != null;
    }
}

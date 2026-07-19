package org.gbl.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CreateContactValidator {

    public CreateContactValidation validate(CreateContactForm form) {
        final var name = form.name().trim();
        final var nameError = name.isBlank() ? "Enter a contact name." : null;

        LocalDate birthdate = null;
        String birthdateError = null;
        try {
            birthdate = LocalDate.parse(form.birthdate());
        } catch (DateTimeParseException exception) {
            birthdateError = "Enter a valid birthday.";
        }

        return new CreateContactValidation(
                name, birthdate, nameError, birthdateError);
    }
}

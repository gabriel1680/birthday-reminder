package org.gbl.controller;

import org.gbl.form.CreateContactForm;
import org.gbl.validation.CreateContactValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateContactValidatorTest {

    private final CreateContactValidator validator = new CreateContactValidator();

    @Test
    void should_validate_and_parse_a_contact_form() {
        final var validation = validator.validate(
                new CreateContactForm("  Ada Lovelace  ", "1815-12-10"));

        assertThat(validation.hasErrors()).isFalse();
        assertThat(validation.name()).isEqualTo("Ada Lovelace");
        assertThat(validation.birthdate()).isEqualTo(LocalDate.parse("1815-12-10"));
    }

    @Test
    void should_report_invalid_fields_without_knowing_the_view_model() {
        final var validation = validator.validate(
                new CreateContactForm("  ", "not-a-date"));

        assertThat(validation.hasErrors()).isTrue();
        assertThat(validation.nameError()).isEqualTo("Enter a contact name.");
        assertThat(validation.birthdateError()).isEqualTo("Enter a valid birthday.");
    }

    @Test
    void should_use_the_same_birthday_error_when_the_field_is_missing() {
        final var validation = validator.validate(
                new CreateContactForm("Ada Lovelace", null));

        assertThat(validation.birthdateError()).isEqualTo("Enter a valid birthday.");
    }
}

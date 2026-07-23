package org.gbl.validation;

import org.gbl.form.CreateContactForm;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateContactValidatorTest {

    private final CreateContactValidator sut = new CreateContactValidator();

    @Test
    void should_validate_and_parse_a_contact_form() {
        final var form = new CreateContactForm("  Ada Lovelace  ", "1815-12-10");
        final var validation = sut.validate(form);
        assertThat(validation.hasErrors()).isFalse();
        assertThat(validation.name()).isEqualTo("Ada Lovelace");
        assertThat(validation.birthdate()).isEqualTo(LocalDate.parse("1815-12-10"));
    }

    @Test
    void should_report_invalid_fields() {
        final var form = new CreateContactForm("  ", "not-a-date");
        final var validation = sut.validate(form);
        assertThat(validation.hasErrors()).isTrue();
        assertThat(validation.nameError()).isEqualTo("Enter a contact name.");
        assertThat(validation.birthdateError()).isEqualTo("Enter a valid birthday.");
    }

    @Test
    void should_use_the_same_birthday_error_when_the_field_is_missing() {
        final var form = new CreateContactForm("Ada Lovelace", null);
        final var validation = sut.validate(form);
        assertThat(validation.birthdateError()).isEqualTo("Enter a valid birthday.");
    }
}

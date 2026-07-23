package org.gbl.validation;

import org.gbl.form.CreateNotificationForm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateNotificationValidatorTest {

    private final CreateNotificationValidator sut = new CreateNotificationValidator();

    @Test
    void should_validate_and_parse_a_notification_form() {
        final var form = new CreateNotificationForm("  email  ", "test@test.com");
        final var validation = sut.validate(form);
        assertThat(validation.hasErrors()).isFalse();
        assertThat(validation.type()).isEqualTo("email");
        assertThat(validation.value()).isEqualTo("test@test.com");
    }

    @Test
    void should_report_invalid_fields() {
        final var form = new CreateNotificationForm("  ", "not-an-email");
        final var validation = sut.validate(form);
        assertThat(validation.hasErrors()).isTrue();
        assertThat(validation.typeError()).isEqualTo("Enter a valid notification type.");
        assertThat(validation.valueError()).isEqualTo("Enter a valid email address.");
    }
}

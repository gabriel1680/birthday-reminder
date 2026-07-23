package org.gbl.controller.notifications;

public final class CreateNotificationValidator {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    public CreateNotificationValidation validate(CreateNotificationForm form) {
        final var type = form.type();
        final var value = form.value();
        return new CreateNotificationValidation(
                type,
                value,
                getTypeError(type),
                getValueError(value));
    }

    private static String getTypeError(String type) {
        return !type.equals("email") ? "Enter a valid notification type." : "";
    }

    private static String getValueError(String value) {
        return !value.matches(EMAIL_PATTERN) ? "Enter a valid email address." : "";
    }
}

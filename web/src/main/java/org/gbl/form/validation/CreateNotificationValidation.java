package org.gbl.form.validation;

public record CreateNotificationValidation(String type, String value, String typeError,
                                           String valueError) {
    public boolean hasErrors() {
        return !typeError.isBlank() || !valueError.isBlank();
    }
}

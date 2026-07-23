package org.gbl.controller.notifications;

public record CreateNotificationValidation(String type, String value, String typeError,
                                           String valueError) {
    public boolean hasErrors() {
        return !typeError.isBlank() || !valueError.isBlank();
    }
}

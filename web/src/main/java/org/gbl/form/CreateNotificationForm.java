package org.gbl.form;

public record CreateNotificationForm(String type, String value) {

    public CreateNotificationForm {
        type = valueOrEmpty(type).trim();
        value = valueOrEmpty(value).trim();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
